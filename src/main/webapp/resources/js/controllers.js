'use strict';


myapp.controller('LoginController', function ($rootScope, $scope, AuthSharedService) {
    $scope.rememberMe = true;
    $scope.login = function () {
        $rootScope.authenticationError = false;
        AuthSharedService.login(
            $scope.username,
            $scope.password,
            $scope.rememberMe
        );
    }
})

    .controller('HomeController', function ($rootScope, $scope, HomeService) {
        $rootScope.provisionMode = false;
        $scope.technos = HomeService.getTechno();
    })
    .controller('UsersController', ['$rootScope', '$scope', '$location', '$http', 'UsersService', 'userService', 'adminService', function ($rootScope, $scope, $location, $http, UsersService, userService, adminService) {

        $rootScope.provisionMode = false;

        $scope.usersSuper = UsersService.getAll();

            $http({
                url: './current_user',
                method: "GET"
            }).then(function (response) {
                //console.log("Worked!");
                //console.log(response.data);
                $scope.user = response.data;
                userService.setUser($scope.user.login);
                userService.setAuth($scope.user.authorities[0].name);
                getCourses($scope.user);
            });

        function getCourses(user) {
                $http({
                    url: './admin_courses',
                    method: "GET",
                    headers: {'email': user.email}
                }).then(function (response) {
                    //console.log("Worked!");
                    //console.log(response.data);
                    $scope.courses = response.data;
                });
            }

            $scope.selectedCourseChanged = function () {
                $scope.course = $scope.adminCourse.value.course;
                adminService.setCourse($scope.adminCourse.value.course);
                /*$rootScope.rawWeekBeginning = $scope.SelectedWeekBeginning.weekBeginning;
                $rootScope.rawWeekEnding = $scope.SelectedWeekEnding.weekEnding;*/
                $http({
                    url: './course_students',
                    method: "GET",
                    headers: {'course': $scope.course}
                }).then(function (response) {
                    //console.log("Worked!");
                    //console.log(response.data);
                    $scope.usersAdmin = response.data;
                });
            };


        $scope.viewProfileSuper = function (email, auth) {
            if (auth == 'student') {
                //console.log("Email: " + email);
                //console.log("Auth: " + auth);
                //console.log("Path: /studentProfile/" + email);
                $location.path('/studentProfile/' + email);
            }
            if (auth == 'admin') {
                //console.log("Email: " + email);
                //console.log("Auth: " + auth);
                //console.log("Path: /studentProfile/" + email);
                $location.path('/adminProfile/' + email);
            }
            if (auth == 'rest') {
                //console.log("Email: " + email);
                //console.log("Auth: " + auth);
                //console.log("Path: /studentProfile/" + email);
                $location.path('/restProfile/' + email);
            }
        };

        $scope.viewProfileAdmin = function (email, auth) {
                //console.log("Email: " + email);
                //console.log("Auth: " + auth);
                //console.log("Path: /studentProfile/" + email);
                $location.path('/studentProfile/' + email);
        }
    }])
    .controller('StudentProfileController', ['$scope', '$rootScope','$location', '$routeParams', '$http', 'userService', '$window', 'adminService', function ($scope, $rootScope, $location, $routeParams, $http, userService, $window, adminService) {
        $scope.userid = $routeParams.user_id;

        $http({
            url: './current_user',
            method: "GET"
        }).then(function (response) {
            //console.log("Worked!");
            //console.log(response.data);
            $scope.user = response.data;
            userService.setUser($scope.user.login);
            userService.setAuth($scope.user.authorities[0].name);
            getAccess();
        });

        function getAccess() {
            $http({
                url: './check_profileaccess',
                method: "GET",
                headers: {'login': userService.getUser(), 'auth': userService.getAuth(), 'email': $scope.userid}
            }).then(function (response) {
                //console.log("Worked!");
                //console.log(response.data);
                if (response.data == false) {
                    $location.path('/home');
                } else {
                    getUserData();
                }
            });
        }

        function getUserData() {
            $http({
                url: './user_email',
                method: "GET",
                headers: {'email': $scope.userid}
            }).then(function (response) {
                //console.log("Worked!");
                //console.log(response.data);
                $scope.userData = response.data;
                getCoursesData();
            });
        }

        function getCoursesData() {
            $http({
                url: './student_courses',
                method: "GET",
                headers: {'email': $scope.userid}
            }).then(function (response) {
                //console.log("Worked!");
                //console.log(response.data);
                $scope.courses = response.data;
            });
        }

        $scope.selectedCourseChanged = function () {
            $rootScope.Period.start = null;
            $rootScope.Period.end = null;
            $http({
                url: './student_teams',
                method: "GET",
                headers: {'course': $scope.studentCourse.value.course, 'email': $scope.userid}
            }).then(function (response) {
                $scope.course = $scope.studentCourse.value.course;
                //console.log(response.data);
                $scope.teams = response.data;
                //console.log($scope.teams);
            }, function (response) {
                //fail case
                //console.log("didn't work");
                //console.log(response);
            });
        };



        $scope.selectedTeamChanged = function () {
            //console.log(response.data);
            $scope.team = $scope.studentTeam.value.team;
            //console.log($scope.team);
        };

        $scope.studentTeamRemove = function () {
            $http({
                url: './studentProfileDelTeam',
                method: "DELETE",
                headers: {
                    'course': $scope.course,
                    'team': $scope.studentTeam.value.team,
                    'email': $scope.userid
                }
            }).then(function (response) {
                $scope.message = "Student Removed From Team";
                $window.alert($scope.message);
            }, function (response) {
                $scope.message = "Student Not Removed From Team";
                $window.alert($scope.message);
            });
        };

        $scope.disableStudent = function () {
            if(!$scope.studentCourse){
                $scope.message = "Please select a course";
                $window.alert($scope.message);
            }else{
                if(!$scope.studentTeam){
                    $scope.message = "Please select a team";
                    $window.alert($scope.message);
                }else{
                    $http({
                        url: './studentDisable',
                        method: "PUT",
                        headers: {'course': $scope.course,
                            'team': $scope.studentTeam.value.team,
                            'email': $scope.userid}
                    }).then(function (response) {
                        $scope.message = "Student Successfully Disabled";
                        $window.alert($scope.message);
                    }, function (response) {
                        $scope.message = "Student Not Disabled";
                        $window.alert($scope.message);
                    });
                }
            }

        };

        $scope.enableStudent = function () {
            if(!$scope.studentCourse){
                $scope.message = "Please select a course";
                $window.alert($scope.message);
            }else{
                if(!$scope.studentTeam){
                    $scope.message = "Please select a team";
                    $window.alert($scope.message);
                }else{
                    $http({
                        url: './studentEnable',
                        method: "PUT",
                        headers: {'course': $scope.course,
                            'team': $scope.studentTeam.value.team,
                            'email': $scope.userid}
                    }).then(function (response) {
                        $scope.message = "Student Successfully Enabled";
                        $window.alert($scope.message);
                    }, function (response) {
                        $scope.message = "Student Not Enabled";
                        $window.alert($scope.message);
                    });
                }
            }

        };

        $scope.studentCourseRemove = function () {
            $http({
                url: './studentProfileDelCourse',
                method: "DELETE",
                headers: {'course': $scope.course, 'email': $scope.userid}
            }).then(function (response) {
                $scope.message = "Student Removed From Course";
                $window.alert($scope.message);
            }, function (response) {
                $scope.message = "Student Not Removed From Course";
                $window.alert($scope.message);
            });
        };

        $scope.removeUser = function () {
            $http({
                url: './userProfileDelete',
                method: "DELETE",
                headers: {'email': $scope.userid}
            }).then(function (response) {
                $scope.message = "User Successfully Deleted";
                $window.alert($scope.message);
            }, function (response) {
                $scope.message = "User Not Deleted";
                $window.alert($scope.message);
            });
        };

        $scope.disableUser = function () {
            $http({
                url: './userDisable',
                method: "PUT",
                headers: {'email': $scope.userid}
            }).then(function (response) {
                $scope.message = "User Successfully Disabled";
                $window.alert($scope.message);
            }, function (response) {
                $scope.message = "User Not Disabled";
                $window.alert($scope.message);
            });
        };

        $scope.enableUser = function () {
            $http({
                url: './userEnable',
                method: "PUT",
                headers: {'email': $scope.userid}
            }).then(function (response) {
                $scope.message = "User Successfully Enabled";
                $window.alert($scope.message);
            }, function (response) {
                $scope.message = "User Not Enabled";
                $window.alert($scope.message);
            });
        };

        $scope.submit = function () {
            if($scope.password.new!=$scope.password.confirm){
                $scope.message = "Passwords Do Not Match";
                $window.alert($scope.message);
            }else {
                $http({
                    url: './userPasswordUpdate',
                    method: "PUT",
                    headers: {'login': $scope.userid, 'pass': $scope.password.new}
                }).then(function (response) {
                    $scope.message = "Password Succesfully Changed";
                    $window.alert($scope.message);
                    $scope.password.new = "";
                    $scope.password.confirm = "";
                }, function (response) {
                    $scope.message = "Password Not Changed";
                    $window.alert($scope.message);
                });
            }
        };

        $scope.adminCourse = adminService.getCourse();
        $http({
            url: './student_teams',
            method: "GET",
            headers: {'course': $scope.adminCourse, 'email': $scope.userid}
        }).then(function (response) {
            $scope.adminTeams = response.data;
            //console.log($scope.adminTeams);
        }, function (response) {
            //fail case
            //console.log("didn't work");
            //console.log(response);
        });

        $scope.selectedTeamChangedAdmin = function () {
            //console.log(response.data);
            $scope.teamAdmin = $scope.studentTeamAdmin.value.team;
            //console.log($scope.teamAdmin);
        };

        $scope.studentTeamRemoveAdmin = function () {
            $http({
                url: './studentProfileDelTeam',
                method: "DELETE",
                headers: {
                    'course': $scope.adminCourse,
                    'team': $scope.teamAdmin,
                    'email': $scope.userid
                }
            }).then(function (response) {
                $scope.message = "Student Removed From Team";
                $window.alert($scope.message);
            }, function (response) {
                $scope.message = "Student Not Removed From Team";
                $window.alert($scope.message);
            });
        };

        $scope.disableStudentAdmin = function () {
            if(!$scope.adminCourse){
                $scope.message = "Please select a course";
                $window.alert($scope.message);
            }else{
                if(!$scope.teamAdmin){
                    $scope.message = "Please select a team";
                    $window.alert($scope.message);
                }else{
                    $http({
                        url: './studentDisable',
                        method: "PUT",
                        headers: {'course': $scope.adminCourse,
                            'team': $scope.teamAdmin,
                            'email': $scope.userid}
                    }).then(function (response) {
                        $scope.message = "Student Successfully Disabled";
                        $window.alert($scope.message);
                    }, function (response) {
                        $scope.message = "Student Not Disabled";
                        $window.alert($scope.message);
                    });
                }
            }

        };

        $scope.enableStudentAdmin = function () {
            if(!$scope.adminCourse){
                $scope.message = "Please select a course";
                $window.alert($scope.message);
            }else{
                if(!$scope.teamAdmin){
                    $scope.message = "Please select a team";
                    $window.alert($scope.message);
                }else{
                    $http({
                        url: './studentEnable',
                        method: "PUT",
                        headers: {'course': $scope.adminCourse,
                            'team': $scope.teamAdmin,
                            'email': $scope.userid}
                    }).then(function (response) {
                        $scope.message = "Student Successfully Enabled";
                        $window.alert($scope.message);
                    }, function (response) {
                        $scope.message = "Student Not Enabled";
                        $window.alert($scope.message);
                    });
                }
            }

        };

        $scope.studentCourseRemoveAdmin = function () {
            $http({
                url: './studentProfileDelCourse',
                method: "DELETE",
                headers: {'course': $scope.adminCourse, 'email': $scope.userid}
            }).then(function (response) {
                $scope.message = "Student Removed From Course";
                $window.alert($scope.message);
            }, function (response) {
                $scope.message = "Student Not Removed From Course";
                $window.alert($scope.message);
            });
        };

    }])
    .controller('RestProfileController', ['$scope', '$location', '$routeParams', '$http', 'userService', '$window', function ($scope, $location, $routeParams, $http, userService, $window) {
        $scope.userid = $routeParams.user_id;

        $scope.password = {};

        $http({
            url: './current_user',
            method: "GET"
        }).then(function (response) {
            //console.log("Worked!");
            //console.log(response.data);
            $scope.user = response.data;
            userService.setUser($scope.user.login);
            userService.setAuth($scope.user.authorities[0].name);
            getAccess();
        });

        function getAccess() {
            $http({
                url: './check_profileaccess',
                method: "GET",
                headers: {'login': userService.getUser(), 'auth': userService.getAuth(), 'email': $scope.userid}
            }).then(function (response) {
                //console.log("Worked!");
                //console.log(response.data);
                if (response.data == false) {
                    $location.path('/home');
                } else {
                    getUserData();
                }
            });
        }

        function getUserData() {
            $http({
                url: './user_email',
                method: "GET",
                headers: {'email': $scope.userid}
            }).then(function (response) {
                //console.log("Worked!");
                //console.log(response.data);
                $scope.userData = response.data;
            });
        }

        $scope.removeUser = function () {
            $http({
                url: './userProfileDelete',
                method: "DELETE",
                headers: {'email': $scope.userid}
            }).then(function (response) {
                $scope.message = "User Account Deleted";
                $window.alert($scope.message);
            }, function (response) {
                $scope.message = "User Account Not Deleted";
                $window.alert($scope.message);
            });
        };

        $scope.disableUser = function () {
            $http({
                url: './userDisable',
                method: "PUT",
                headers: {'email': $scope.userid}
            }).then(function (response) {
                $scope.message = "User Successfully Disabled";
                $window.alert($scope.message);
            }, function (response) {
                $scope.message = "User Not Disabled";
                $window.alert($scope.message);
            });
        };

        $scope.enableUser = function () {
            $http({
                url: './userEnable',
                method: "PUT",
                headers: {'email': $scope.userid}
            }).then(function (response) {
                $scope.message = "User Successfully Enabled";
                $window.alert($scope.message);
            }, function (response) {
                $scope.message = "User Not Enabled";
                $window.alert($scope.message);
            });
        };

        $scope.submit = function () {
            if($scope.password.new!=$scope.password.confirm){
                $scope.message = "Passwords Do Not Match";
                $window.alert($scope.message);
            }else {
                $http({
                    url: './userPasswordUpdate',
                    method: "PUT",
                    headers: {'login': $scope.userid, 'pass': $scope.password.new}
                }).then(function (response) {
                    $scope.message = "Password Succesfully Changed";
                    $window.alert($scope.message);
                    $scope.password.new = "";
                    $scope.password.confirm = "";
                }, function (response) {
                    $scope.message = "Password Not Changed";
                    $window.alert($scope.message);
                });
            }
        }



    }])

    .controller('AdminProfileController', ['$scope', '$rootScope', '$location', '$routeParams', '$http', 'userService', '$window', function ($scope, $rootScope, $location, $routeParams, $http, userService, $window) {
        $scope.userid = $routeParams.user_id;

        $http({
            url: './current_user',
            method: "GET"
        }).then(function (response) {
            //console.log("Worked!");
            //console.log(response.data);
            $scope.user = response.data;
            userService.setUser($scope.user.login);
            userService.setAuth($scope.user.authorities[0].name);
            getAccess();
        });

        function getAccess() {
            $http({
                url: './check_profileaccess',
                method: "GET",
                headers: {'login': userService.getUser(), 'auth': userService.getAuth(), 'email': $scope.userid}
            }).then(function (response) {
                //console.log("Worked!");
                //console.log(response.data);
                if (response.data == false) {
                    $location.path('/home');
                } else {
                    getUserData();
                }
            });
        }

        function getUserData() {
            $http({
                url: './user_email',
                method: "GET",
                headers: {'email': $scope.userid}
            }).then(function (response) {
                //console.log("Worked!");
                //console.log(response.data);
                $scope.userData = response.data;
                getCoursesData();
            });
        }

        function getCoursesData() {
            $http({
                url: './admin_courses',
                method: "GET",
                headers: {'email': $scope.userid}
            }).then(function (response) {
                //console.log("Worked!");
                //console.log(response.data);
                $scope.courses = response.data;
            });
        }

        $scope.selectedCourseChanged = function () {
            $scope.course = $scope.adminCourse.value.course;
            $rootScope.Period.start = null;
            $rootScope.Period.end = null;
        };

        $scope.adminCourseRemove = function () {
            $http({
                url: './adminProfileDelete',
                method: "DELETE",
                headers: {'course': $scope.adminCourse.value.course, 'email': $scope.userid}
            }).then(function (response) {
                $scope.message = "Admin Removed From Course";
                $window.alert($scope.message);
            }, function (response) {
                $scope.message = "Admin Not Removed From Course";
                $window.alert($scope.message);
            });
        };

        $scope.removeUser = function () {
            $http({
                url: './userProfileDelete',
                method: "DELETE",
                headers: {'email': $scope.userid}
            }).then(function (response) {
                $scope.message = "User Successfully Deleted";
                $window.alert($scope.message);
            }, function (response) {
                $scope.message = "User Not Deleted";
                $window.alert($scope.message);
            });
        };

        $scope.disableUser = function () {
            $http({
                url: './userDisable',
                method: "PUT",
                headers: {'email': $scope.userid}
            }).then(function (response) {
                $scope.message = "User Successfully Disabled";
                $window.alert($scope.message);
            }, function (response) {
                $scope.message = "User Not Disabled";
                $window.alert($scope.message);
            });
        };

        $scope.enableUser = function () {
            $http({
                url: './userEnable',
                method: "PUT",
                headers: {'email': $scope.userid}
            }).then(function (response) {
                $scope.message = "User Successfully Enabled";
                $window.alert($scope.message);
            }, function (response) {
                $scope.message = "User Not Enabled";
                $window.alert($scope.message);
            });
        };

        $scope.submit = function () {
            if($scope.password.new!=$scope.password.confirm){
                $scope.message = "Passwords Do Not Match";
                $window.alert($scope.message);
            }else {
                $http({
                    url: './userPasswordUpdate',
                    method: "PUT",
                    headers: {'login': $scope.userid, 'pass': $scope.password.new}
                }).then(function (response) {
                    $scope.message = "Password Successfully Changed";
                    $window.alert($scope.message);
                    $scope.password.new = "";
                    $scope.password.confirm = "";
                }, function (response) {
                    $scope.message = "Password Not Changed";
                    $window.alert($scope.message);
                });
            }
        }


    }])
    .controller('ApiDocController', function ($rootScope, $scope) {

    $rootScope.provisionMode = false;
    // init form
    $scope.isLoading = false;
    $scope.url = $scope.swaggerUrl = 'v2/api-docs';
    // error management
    $scope.myErrorHandler = function (data, status) {
        console.log('failed to load swagger: ' + status + '   ' + data);
    };

    $scope.infos = false;
})
    .controller('MetricsGuideController', function ($rootScope, $scope) {

        $rootScope.provisionMode = false;
    })
    .controller('TokensController', function ($rootScope, $scope, UsersService, TokensService, $q) {

        $rootScope.provisionMode = false;

        var browsers = ["Firefox", 'Chrome', 'Trident'];

        $q.all([
            UsersService.getAll().$promise,
            TokensService.getAll().$promise
        ]).then(function (data) {
            var users = data[0];
            var tokens = data[1];

            tokens.forEach(function (token) {
                users.forEach(function (user) {
                    if (token.userLogin === user.login) {
                        token.firstName = user.firstName;
                        token.familyName = user.familyName;
                        browsers.forEach(function (browser) {
                            if (token.userAgent.indexOf(browser) > -1) {
                                token.browser = browser;
                            }
                        });
                    }
                });
            });

            $scope.tokens = tokens;
        });


    })
    .controller('LogoutController', function (AuthSharedService) {
        AuthSharedService.logout();
    })
    .controller('ErrorController', function ($scope, $routeParams) {
        $scope.code = $routeParams.code;

        switch ($scope.code) {
            case "403" :
                $scope.message = "Oops! you have come to unauthorized page.";
                break;
            case "404" :
                $scope.message = "Page not found.";
                break;
            default:
                $scope.code = 500;
                $scope.message = "Oops! unexpected error"
        }

    })
    .controller('NavController', ['$scope', '$rootScope','$location', '$http', 'courseService', 'teamService', 'studentService', '$window', '$routeParams', 'userService',
        function ($scope, $rootScope, $location, $http, courseService, teamService, studentService, $window, $routeParams, userService) {

            $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";

            function getCourses(user) {
                if (userService.getAuth() == 'super_user') {
                    $http({
                        url: './taigaCourses',
                        method: "GET"
                    }).then(function (response) {
                        //console.log("Worked!");
                        //console.log(response.data);
                        $scope.courses = response.data;
                    });
                }
                if (userService.getAuth() == 'admin') {
                    $http({
                        url: './admin_courses',
                        method: "GET",
                        headers: {'email': user.email}
                    }).then(function (response) {
                        //console.log("Worked!");
                        //console.log(response.data);
                        $scope.courses = response.data;
                    });
                }
                if (userService.getAuth() == 'student') {
                    $http({
                        url: './student_courses',
                        method: "GET",
                        headers: {'email': user.email}
                    }).then(function (response) {
                        //console.log("Worked!");
                        //console.log(response.data);
                        $scope.courses = response.data;
                    });
                }

            }

            $http({
                url: './current_user',
                method: "GET"
            }).then(function (response) {
                //console.log("Worked!");
                //console.log(response.data);
                $scope.user = response.data;
                userService.setUser($scope.user.login);
                userService.setAuth($scope.user.authorities[0].name);
                getCourses($scope.user);
            });

            $scope.selectedCourseChanged = function (course) {
                if (userService.getAuth() == 'super_user') {
                    courseService.setCourse(course);
                    /*$rootScope.rawWeekBeginning = null;
                    $rootScope.rawWeekEnding = null;*/
                    $http({
                        url: './course_teams',
                        method: "GET",
                        headers: {'course': course}
                    }).then(function (response) {
                        //console.log("Worked!");
                        //console.log(response.data);
                        $scope.teams = response.data;
                    });
                }
                if (userService.getAuth() == 'admin') {
                    courseService.setCourse(course);
                    $http({
                        url: './course_teams',
                        method: "GET",
                        headers: {'course': course}
                    }).then(function (response) {
                        //console.log("Worked!");
                        //console.log(response.data);
                        $scope.teams = response.data;
                    });
                }
                if (userService.getAuth() == 'student') {
                    courseService.setCourse(course);
                    $http({
                        url: './student_teams',
                        method: "GET",
                        headers: {'course': course, 'email': $scope.user.email}
                    }).then(function (response) {
                        //console.log("Worked!");
                        //console.log(response.data);
                        $scope.teams = response.data;
                    });
                }
            };

            $scope.selectedTeamChanged = function (team) {
                if (userService.getAuth() == 'super_user') {
                    teamService.setTeam(team);
                    $http({
                        url: './team_students',
                        method: "GET",
                        headers: {'course': courseService.getCourse(), 'team': teamService.getTeam()}
                    }).then(function (response) {
                        //console.log("Worked!");
                        //console.log(response.data);
                        $scope.students = response.data;
                    });
                }
                if (userService.getAuth() == 'admin') {
                    teamService.setTeam(team);
                    $http({
                        url: './team_students',
                        method: "GET",
                        headers: {'course': courseService.getCourse(), 'team': teamService.getTeam()}
                    }).then(function (response) {
                        //console.log("Worked!");
                        //console.log(response.data);
                        $scope.students = response.data;
                    });
                }
                if (userService.getAuth() == 'student') {
                    teamService.setTeam(team);
                    //console.log("EMail1: " + userService.getUser());
                    //console.log("EMail2: " + $scope.user.login);
                    $http({
                        url: './student_data',
                        method: "GET",
                        headers: {
                            'email': $scope.user.login,
                            'course': courseService.getCourse(),
                            'team': teamService.getTeam()
                        }
                    }).then(function (response) {
                        //console.log("Worked!");
                        //console.log(response.data);
                        $scope.students = response.data;
                    });

                }

            };
            $scope.selectedStudentChanged = function (email, full_name) {
                studentService.setStudentEmail(email);
                studentService.setStudentName(full_name);
            }
        }])

    .controller('TabController', ['$scope', function ($scope) {
        $scope.tab = 1;

        $scope.setTab = function (newTab) {
            $scope.tab = newTab;
        };

        $scope.isSet = function (tabNum) {
            return $scope.tab === tabNum;
        };
    }])
    .controller('RegistrationController', ['$scope', '$location', '$http', '$window', function ($scope, $location, $http, $window) {
        $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";

        $scope.roles = {
            role1: {name: "super_user"},
            role2: {name: "rest"},
            role3: {name: "admin"},
            role4: {name: "student"}
        };
        $scope.role = "";

        $scope.sendRegistration = function () {
            $http({
                url: './user',
                method: "POST",
                headers: {
                    'first_name': $scope.firstName, 'family_name': $scope.familyName, 'email': $scope.email,
                    'password': $scope.password, 'role': $scope.selectedRole.value.name
                }
            }).then(function (response) {
                console.log("Worked!");
                $scope.responseData = console.log(response.data);
                $scope.message = "User Successfully Registered";
                $window.alert($scope.message);
                $location.path('/users');
            }, function (response) {
                //fail case
                //console.log("Role: " + $scope.selectedRole.value.name);
                //console.log("Didn't work");
                console.log(response);
                $scope.responseData = console.log(response.data);
                $scope.message = "User Not Registered, Duplicate User or Incorrect Information";
                $window.alert($scope.message);
            });

        };

    }])
    .controller('CourseController', ['$scope', '$rootScope', '$routeParams', '$location', 'courseService', 'userService', '$http', function ($scope, $rootScope, $routeParams, $location, courseService, userService, $http) {
        if ($routeParams.course_id != null) {
            $scope.courseid = $routeParams.course_id;
        } else {
            $scope.courseid = "none";
        }
        //console.log("course: " + $scope.courseid);
        var initial = true;

        $http({
            url:'/course_data',
            method:"get",
            headers: {'course':$scope.courseid}
        }).then(function (response) {

            $scope.startDate = response.data.start_date;
            var end_date = response.data.end_date;
            var now = new Date();
            $scope.endDate = (end_date.value>now.value?now:end_date);
        },function (response) {
            console.log("Couldn't retrieve course data")
        });

        $http({
            url: './ag_url',
            method: "GET"
        }).then(function (response) {
            $scope.autograder_url = response.data;
            //console.log($scope.autograder_url);
        }, function (response) {
            //fail case
            console.log("didn't work: " + response.data);
        });

        $scope.commitMaxY = 0;

        $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";
        $http({
            url: './check_courseaccess',
            method: "GET",
            headers: {'course': $scope.courseid, 'login': userService.getUser(), 'auth': userService.getAuth()}
        }).then(function (response) {
            //console.log("Worked!");
            //console.log(response.data);
            if (response.data == false) {
                $location.path('/home');
            } else {
                initial = true;
                getGithubIntervals();
            }
        }, function (response) {
            //fail case
            console.log("didn't work");
            $location.path('/home');
        });

        function getTaigaWeightFreq() {
            $http({
                url: './taiga/course_weightFreq',
                method: "GET",
                headers: {
                    'course': $scope.courseid,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                $scope.courseArrayTG = response.data;
                getGitHubWeightFreq();
            }, function (response) {
                //fail case
                console.log("didn't work");
                getGitHubWeightFreq();
            });
        }
        function getGitHubWeightFreq() {
            $http({
                url: './github/course_weightFreq',
                method: "GET",
                headers: {
                    'course': $scope.courseid,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                $scope.courseArrayGH = response.data;
                getSlackWeightFreq();
            }, function (response) {
                //fail case
                console.log("didn't work");
                getSlackWeightFreq();
            });
        }

        function getSlackWeightFreq() {
            $http({
                url: './slack/course_weightFreq',
                method: "GET",
                headers: {
                    'course': $scope.courseid,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                $scope.courseArraySK = response.data;
                plotCurrentWeek();
            }, function (response) {
                //fail case
                console.log("didn't work");
                plotCurrentWeek();
            });
        }

        function plotCurrentWeek() {

            if($scope.courseArrayTG[0] == null){
                $scope.courseWeightTG0 = 0;
                $scope.courseFrequencyTG0 = 0;
            }else{
                $scope.courseWeightTG0 = $scope.courseArrayTG[0].weight;
                $scope.courseFrequencyTG0 = $scope.courseArrayTG[0].frequency;
            }
            if($scope.courseArrayGH[0] == null){
                $scope.courseWeightGH0 = 0;
                $scope.courseFrequencyGH0 = 0;
            }else{
                $scope.courseWeightGH0 = $scope.courseArrayGH[0].weight;
                $scope.courseFrequencyGH0 = $scope.courseArrayGH[0].frequency;
            }
            if($scope.courseArraySK[0] == null){
                $scope.courseWeightSK0 = 0;
                $scope.courseFrequencySK0 = 0;
            }else{
                $scope.courseWeightSK0 = $scope.courseArraySK[0].weight;
                $scope.courseFrequencySK0 = $scope.courseArraySK[0].frequency;
            }

            $scope.selectedWeekLabels = ['Taiga Impact', 'Taiga Freq', 'GH Impact', 'GH Freq', 'Slack Impact', 'Slack Freq'];
            $scope.selectedWeekSeries = "Course";
            $scope.selectedWeekData = [$scope.courseWeightTG0, $scope.courseFrequencyTG0, $scope.courseWeightGH0, $scope.courseFrequencyGH0, $scope.courseWeightSK0, $scope.courseFrequencySK0];

            if(initial){
                //console.log("Initial Run for Selected Chart");
                var selectedOverallCtx = $("#radarSelected").get(0).getContext("2d");
                $scope.selectedOverallChart = new Chart(selectedOverallCtx, {
                    type: 'radar',
                    data: {
                        labels: $scope.selectedWeekLabels,
                        datasets: [{
                            fill: 'origin',
                            backgroundColor:'rgb(228, 98, 98,.25)',
                            borderColor: 'rgb(228, 98, 98)',
                            borderWidth: 2,
                            label: $scope.selectedWeekSeries,
                            data: $scope.selectedWeekData,
                            pointRadius: 3,
                            pointBorderWidth: 2,
                            pointBackgroundColor: 'rgb(228, 98, 98,.25)',
                            pointBorderColor: 'rgb(228, 98, 98)',
                            pointHoverRadius: 6
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        legend: {
                            display: true
                        },
                        scale: {
                            ticks: {
                                beginAtZero: true,
                                min: 0,
                                max: 3,
                                stepSize: .5
                            },
                            pointLabels: {
                                fontSize: 14
                            }
                        }
                    }
                });
                initial = false;
                plotPreviousWeek();
            } else {
                //console.log("Not Initial Run for Selected Chart");
                $('#radarSelected').remove();
                $('#selectedOverall').append('<canvas id="radarSelected" class="chart" style="width:100%; height:100%"></canvas>');
                var selectedOverallCtx = $("#radarSelected").get(0).getContext("2d");
                $scope.selectedOverallChart = new Chart(selectedOverallCtx, {
                    type: 'radar',
                    data: {
                        labels: $scope.selectedWeekLabels,
                        datasets: [{
                            fill: 'origin',
                            backgroundColor:'rgb(228, 98, 98,.25)',
                            borderColor: 'rgb(228, 98, 98)',
                            borderWidth: 2,
                            label: $scope.selectedWeekSeries,
                            data: $scope.selectedWeekData,
                            pointRadius: 3,
                            pointBorderWidth: 2,
                            pointBackgroundColor: 'rgb(228, 98, 98,.25)',
                            pointBorderColor: 'rgb(228, 98, 98)',
                            pointHoverRadius: 6
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        legend: {
                            display: true
                        },
                        scale: {
                            ticks: {
                                beginAtZero: true,
                                min: 0,
                                max: 3,
                                stepSize: .5
                            },
                            pointLabels: {
                                fontSize: 14
                            }
                        }
                    }
                });
            }
        }

        function plotPreviousWeek() {

            if($scope.courseArrayTG[1] == null){
                $scope.courseWeightTG1 = 0;
                $scope.courseFrequencyTG1 = 0;
            }else{
                $scope.courseWeightTG1 = $scope.courseArrayTG[1].weight;
                $scope.courseFrequencyTG1 = $scope.courseArrayTG[1].frequency;
            }
            if($scope.courseArrayGH[1] == null){
                $scope.courseWeightGH1 = 0;
                $scope.courseFrequencyGH1 = 0;
            }else{
                $scope.courseWeightGH1 = $scope.courseArrayGH[1].weight;
                $scope.courseFrequencyGH1 = $scope.courseArrayGH[1].frequency;
            }
            if($scope.courseArraySK[1] == null){
                $scope.courseWeightSK1 = 0;
                $scope.courseFrequencySK1 = 0;
            }else{
                $scope.courseWeightSK1 = $scope.courseArraySK[1].weight;
                $scope.courseFrequencySK1 = $scope.courseArraySK[1].frequency;
            }

            $scope.overallWeekLabels = ['Taiga Impact', 'Taiga Freq', 'GH Impact', 'GH Freq', 'Slack Impact', 'Slack Freq'];
            $scope.overallWeekSeries = "Course";
            $scope.overallWeekData = [$scope.courseWeightTG1, $scope.courseFrequencyTG1, $scope.courseWeightGH1, $scope.courseFrequencyGH1, $scope.courseWeightSK1, $scope.courseFrequencySK1];

            var totalOverallCtx = $("#radarOverall").get(0).getContext("2d");
            $scope.totalOverallChart = new Chart(totalOverallCtx, {
                type: 'radar',
                data: {
                    labels: $scope.overallWeekLabels,
                    datasets: [{
                        fill: 'origin',
                        backgroundColor: 'rgb(228, 98, 98,.25)',
                        borderColor: 'rgb(228, 98, 98)',
                        borderWidth: 2,
                        label: $scope.overallWeekSeries,
                        data: $scope.overallWeekData,
                        pointRadius: 3,
                        pointBorderWidth: 2,
                        pointBackgroundColor: 'rgb(228, 98, 98,.25)',
                        pointBorderColor: 'rgb(228, 98, 98)',
                        pointHoverRadius: 6
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    legend: {
                        display: true
                    },
                    scale: {
                        ticks: {
                            beginAtZero: true,
                            min: 0,
                            max: 3,
                            stepSize: .5
                        },
                        pointLabels: {
                            fontSize: 14
                        }
                    }
                }
            });
        }

        function getTaigaActivity() {
            $scope.taigaMaxY = 0;
            $http({
                url: './taiga/course_activity',
                method: "GET",
                headers: {'course': $scope.courseid}
            }).then(function (response) {
                //console.log("Worked!");
                //console.log(response.data);
                $scope.courseActivity = response.data;
                $scope.dataForTaigaCourseActivity =  getDataForCourseTaigaActivityCharts(response.data);
            }, function (response) {
                //fail case
                console.log("didn't work");
            });
        }

        $scope.optionsForTaigaCourseActivity = {

            chart: {
                type: 'lineChart',
                height: 450,
                margin : {
                    top: 50,
                    right: 150,
                    bottom: 100,
                    left:100
                },

                x: function(d){ return d[0]; },
                y: function(d){ return d[1]; },

                useInteractiveGuideline: true,

                xAxis: {
                    axisLabel: 'Date of Activity',
                    tickFormat: function(d) {
                        return d3.time.format('%Y-%m-%d')(new Date(d))
                    },

                    showMaxMin: false,
                    staggerLabels: false
                },

                yAxis: {
                    axisLabel: 'Taiga Task Activity',
                    axisLabelDistance: 0,
                },

                yDomain:[0, $scope.taigaMaxY]

            }
        };

        function getDataForCourseTaigaActivityCharts(array){

            var data = []; var inProgress = []; var toTest = []; var done = [];var expected = [];var maxArr=[];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];var valueset2 = [];var valueset3 = [];var valueset4 = [];

                valueset1.push(array[i].rawWeekEnding*1000);
                valueset1.push(array[i].inProgressActivity);

                valueset2.push(array[i].rawWeekEnding*1000);
                valueset2.push(array[i].toTestActivity);

                valueset3.push(array[i].rawWeekEnding*1000);
                valueset3.push(array[i].doneActivity);

                valueset4.push(array[i].rawWeekEnding*1000);
                valueset4.push(3);

                inProgress.push(valueset1);
                toTest.push(valueset2);
                done.push(valueset3);
                expected.push(valueset4);
                maxArr.push(array[i].inProgressActivity);
                maxArr.push(array[i].toTestActivity);
                maxArr.push(array[i].doneActivity);
            }
            var taigaLineChartMax = Math.ceil(Math.max.apply(Math, maxArr));
            $scope.taigaMaxY = getTaigaLineChartMax(taigaLineChartMax);

            data.push({color: "#6799ee", key: "IN PROGRESS", values: inProgress, strokeWidth: 2});
            data.push({color: "#000000", key: "READY FOR TEST", values: toTest, strokeWidth: 2});
            data.push({color: "#2E8B57", key: "CLOSED", values: done, strokeWidth: 2});
            data.push({color: "#8f65b6", key: "EXPECTED", values: expected, classed: "dashed"});

            return data;
        }
        function getTaigaLineChartMax(taigaLineChartMax){
            if(taigaLineChartMax > 50){
                return 50;
            } else {
                return taigaLineChartMax;
            }
        }
        function getGithubIntervals() {
            $http({
                url: './github/course_intervals',
                method: "GET",
                headers: {'course': $scope.courseid}
            }).then(function (response) {
                //console.log("Worked!");
                //console.log(response.data);
                $scope.courseIntervals = response.data;
                console.log("Intervals: "+$scope.courseIntervals);

                if ($rootScope.Period.start == null && $rootScope.Period.end == null) {
                    $rootScope.Period.start = new Date($scope.courseIntervals[$scope.courseIntervals.length - 1].weekBeginning);
                    $rootScope.Period.end = new Date($scope.courseIntervals[$scope.courseIntervals.length - 1].weekEnding);
                    if($rootScope.Period.start.valueOf()<Date.parse($scope.startDate))
                        $rootScope.Period.start = new Date(Date.parse($scope.startDate));
                    console.log("initial end: "+$rootScope.Period.end.value);
                    if($rootScope.Period.end.valueOf()>Date.parse($scope.endDate))
                        $rootScope.Period.end = new Date(Date.parse($scope.endDate));
                    $scope.IntervalChangedEnd();
                }
                else {
                    $scope.IntervalChangedEnd();
                }

                /*if ($rootScope.rawWeekBeginning == null && $rootScope.rawWeekEnding == null) {
                    $scope.SelectedWeekBeginning = $scope.courseIntervals[$scope.courseIntervals.length - 1];
                    $rootScope.rawWeekBeginning = $scope.SelectedWeekBeginning.rawWeekBeginning*1000;
                    $scope.SelectedWeekEnding = $scope.courseIntervals[$scope.courseIntervals.length - 1];
                    $scope.IntervalChangedEnd($scope.SelectedWeekEnding.weekEnding);//!*1000);
                }
                else {
                    $scope.SelectedWeekBeginning.weekBeginning = $rootScope.rawWeekBeginning/!*$scope.courseIntervals.find(function(element) {
                        return element.rawWeekBeginning == $rootScope.rawWeekBeginning;
                    });*!/
                    $scope.SelectedWeekEnding.weekEnding = $rootScope.rawWeekEnding/!*$scope.courseIntervals.find(function(element) {
                        return element.rawWeekEnding == $rootScope.rawWeekEnding;
                    });*!/
                    $scope.IntervalChangedEnd($rootScope.rawWeekEnding);
                }*/
                $('select option')
                    .filter(function() {
                        return !this.value || $.trim(this.value).length == 0 || $.trim(this.text).length == 0;
                    })
                    .remove();
            }, function (response) {
                $('select option')
                    .filter(function() {
                        return !this.value || $.trim(this.value).length == 0 || $.trim(this.text).length == 0;
                    })
                    .remove();
                //fail case
                console.log("didn't work");
            });
        }

        $scope.IntervalChangedBegin = function () {
             if ($rootScope.Period.end != null) {
                 if($rootScope.Period.end<$rootScope.Period.start){
                     $rootScope.Period.end = $rootScope.Period.start;
                     $rootScope.Period.end.setHours(23,59,59,999);
                 }
                 $rootScope.Period.start.setHours(0,0,0,0);
                console.log('Calling data');
                getAllTabsData();
            }
        }
        $scope.IntervalChangedEnd = function () {
            if ($rootScope.Period.start != null) {
                if($rootScope.Period.start>$rootScope.Period.end){
                    $rootScope.Period.start = $rootScope.Period.end;
                    $rootScope.Period.start.setHours(0,0,0,0);
                }
                $rootScope.Period.end.setHours(23,59,59,999);
                getAllTabsData();
            }}
        function getAllTabsData(){
            getTaigaWeightFreq();
                $http({
                    url: './taiga/course_tasks',
                    method: "GET",
                    headers: {
                        'course': $scope.courseid,
                        'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                        'weekending': new Date($rootScope.Period.end).valueOf()
                        /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                        'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                    }
                }).then(function (response) {
                    //console.log("Worked, these are the Taiga averages for the week for weekBegin");
                    //console.log(response.data);
                    $scope.courseTasks = response.data;
                    $scope.dataForTaigaCourseTasks = getDataForTaigaCourseTasks(response.data);
                    getTaigaActivity();
                });
                $http({
                    url: './github/commits_course',
                    method: "GET",
                    headers: {
                        'course': $scope.courseid,
                        'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                        'weekending': new Date($rootScope.Period.end).valueOf()
                        /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                        'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                    }
                }).then(function (response) {
                    //console.log("Worked This is what the GitHub Data is showing1: !");
                    processGitHubCommitMax(response.data);

                }, function (response) {
                    //fail case
                    console.log("didn't work");

                });
                $http({
                    url: './slack/course_messages',
                    method: "GET",
                    headers: {
                        'course': $scope.courseid,
                        'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                        'weekending': new Date($rootScope.Period.end).valueOf()
                        /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                        'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                    }
                }).then(function (response) {
                    //console.log("SlackCourseMessages");
                    //console.log(response.data);
                    $scope.courseTasks = response.data;
                    $scope.dataForSlackCourseMessages = getDataForSlackCourseMessages(response.data);
                    getSlackActivity();
                }, function (response) {
                    //fail case
                    console.log("didn't work");
                });
            }

        $scope.optionsForTaigaCourseTasks = {

            chart: {
                type: 'multiBarChart',
                height: 450,
                margin : {
                    top: 50,
                    right: 150,
                    bottom: 100,
                    left:100
                },

                x: function(d){ console.log('Calling this on change?'); return d[0]; },
                y: function(d){ return d[1]; },

                clipEdge: true,
                duration: 500,
                stacked: false,

                xAxis: {
                    axisLabel: 'Date of Activity',
                    showMaxMin: false
                },

                yAxis: {
                    axisLabel: 'Taiga Task Totals',
                    axisLabelDistance: 0
                }
            }
        };

        function getDataForTaigaCourseTasks(array){

            var data = []; var inProgress = []; var toTest = []; var done =[];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];var valueset2 = [];var valueset3 = [];

                valueset1.push(array[i].date);
                valueset1.push(array[i].inProgress);

                valueset2.push(array[i].date);
                valueset2.push(array[i].toTest);

                valueset3.push(array[i].date);
                valueset3.push(array[i].done);

                inProgress.push(valueset1);
                toTest.push(valueset2);
                done.push(valueset3);
            }

            data.push({color: "#6799ee", key: "IN PROGRESS", values: inProgress});
            data.push({color: "#000000", key: "READY FOR TEST", values: toTest});
            data.push({color: "#2E8B57", key: "CLOSED", values: done});

            return data;
        }

        function processGitHubCommitMax(array){
            var commits = []; var linesOfCodeAdded = []; var linesOfCodeDeleted = [];
            for (var i = 0; i < array.length; i++){
                commits.push(array[i].commits);
                linesOfCodeAdded.push(array[i].linesOfCodeAdded/100);
                linesOfCodeDeleted.push(array[i].linesOfCodeDeleted/100);
            }
            var maxArray = [Math.max.apply(Math, commits), Math.max.apply(Math, linesOfCodeAdded), Math.max.apply(Math, linesOfCodeDeleted)];
            var gitHubBarChartMax = Math.ceil(Math.max.apply(Math, maxArray));
            $scope.commitMaxY = getGitHubBarChartMax(gitHubBarChartMax);
            $scope.dataForGitHubCourseCommits =  getDataForGitHubCourseCommitsCharts(array);
            commitOptions();
            getGitHubWeightData();
        }

        function getGitHubBarChartMax(gitHubBarChartMax){

            if(gitHubBarChartMax > 50){
                return 50;
            } else {
                return gitHubBarChartMax;
            }
        }
        function commitOptions() {
            $scope.optionsForGitHubCourseCommits = {

                chart: {
                    type: 'multiBarChart',
                    height: 450,
                    margin: {
                        top: 60,
                        right: 150,
                        bottom: 100,
                        left: 100
                    },

                    legend: {
                        margin: {
                            top: 0,
                            right: 0,
                            bottom: 20,
                            left: 0
                        },
                        maxKeyLength: 100
                    },

                    x: function (d) {
                        return d[0];
                    },
                    y: function (d) {
                        return d[1];
                    },

                    clipEdge: true,
                    duration: 500,
                    stacked: false,

                    xAxis: {
                        axisLabel: 'Date of Activity',
                        showMaxMin: false
                    },

                    yAxis: {
                        axisLabel: 'GitHub Commit Counts',
                        axisLabelDistance: 0
                    },

                    yDomain: [0, $scope.commitMaxY]
                }
            };
        }
        //* Function to Parse GitHub CommitData for MultiBar Chart * //
        function getDataForGitHubCourseCommitsCharts(array){

            var commits = []; var linesOfCodeAdded = []; var linesOfCodeDeleted = []; var data = [];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];var valueset2 = [];var valueset3 = [];

                valueset1.push(array[i].gitHubPK.date);
                valueset1.push(array[i].commits);

                valueset2.push(array[i].gitHubPK.date);
                valueset2.push(array[i].linesOfCodeAdded/100);

                valueset3.push(array[i].gitHubPK.date);
                valueset3.push(array[i].linesOfCodeDeleted/100);

                commits.push(valueset1);
                linesOfCodeAdded.push(valueset2);
                linesOfCodeDeleted.push(valueset3);
            }

            data.push({color: "#6799ee", key: "Commits", values: commits});
            data.push({color: "#000000", key: "Lines Of Code Added/100", values: linesOfCodeAdded});
            data.push({color: "#2E8B57", key: "Lines Of Code Deleted/100", values: linesOfCodeDeleted});

            return data;
        }

        function getGitHubWeightData() {
            $http({
                url: './github/weights_course',
                method: "GET",
                headers: {'course': $scope.courseid}
            }).then(function (response) {
                //console.log("Worked This is what the GitHub Weight is showing: !");
                //console.log(response.data);
                $scope.dataForGitHubCourseWeight= getDataForGitHubCourseWeightCharts(response.data);
                getSlackActivity();
            }, function (response) {
                //fail case
                console.log("didn't work");
                getSlackActivity();

            });
        }

        $scope.optionsForGitHubCourseWeight = {

            chart: {
                type: 'lineChart',
                height: 450,
                margin : {
                    top: 50,
                    right: 150,
                    bottom: 100,
                    left:100
                },

                x: function(d){ return d[0]; },
                y: function(d){ return d[1]; },

                useInteractiveGuideline: true,

                xAxis: {
                    axisLabel: 'Date of Activity',
                    tickFormat: function(d) {
                        return d3.time.format('%Y-%m-%d')(new Date(d))
                    },

                    showMaxMin: false,
                    staggerLabels: false
                },

                yAxis: {
                    axisLabel: 'GitHub Task Updates Weight',
                    axisLabelDistance: 0,
                    tickValues:  [0, 3, 6, 9, 12, 15]
                },

                yDomain:[0, 15]

            }
        };

        ////* Function to Parse GitHub Weight for Line Chart * ////

        function getDataForGitHubCourseWeightCharts(array){

            var weight = []; var expected = []; var data = [];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];var valueset2 = [];

                valueset1.push(Date.parse(array[i].gitHubPK.date));
                valueset1.push(array[i].weight);

                valueset2.push(Date.parse(array[i].gitHubPK.date));
                valueset2.push(4);

                weight.push(valueset1);
                expected.push(valueset2);
            }

            data.push({color: "#6799ee", key: "Weight", values: weight, strokeWidth: 2});
            data.push({color: "#000000", key: "Expected", values: expected, strokeWidth: 2});

            return data;
        }

        function getSlackActivity() {
            $http({
                url: './slack/course_totals',
                method: "GET",
                headers: {'course': $scope.courseid}
            }).then(function (response) {
                //console.log("SlackActivity");
                //console.log(response.data);
                $scope.slackCourseActivity = response.data;
                $scope.dataForSlackCourseActivity = getDataForCourseSlackActivityCharts(response.data);

            }, function (response) {
                //fail case
                console.log("didn't work");

            });
        }

        $scope.optionsForSlackCourseActivity = {

            chart: {
                type: 'lineChart',
                height: 450,
                margin : {
                    top: 50,
                    right: 150,
                    bottom: 100,
                    left:100
                },

                x: function(d){ return d[0]; },
                y: function(d){ return d[1]; },

                useInteractiveGuideline: true,

                xAxis: {
                    axisLabel: 'Date of Activity',
                    tickFormat: function(d) {
                        return d3.time.format('%Y-%m-%d')(new Date(d))
                    },

                    showMaxMin: false,
                    staggerLabels: false
                },

                yAxis: {
                    axisLabel: 'Slack Message Activity',
                    axisLabelDistance: 0,
                    tickValues:  [0, 25, 50, 100, 125, 125, 175, 200]
                },

                yDomain:[0, 200]

            }
        };

        function getDataForCourseSlackActivityCharts(array){

            var data = []; var total = [];var expected = [];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];var valueset2 = [];

                valueset1.push(Date.parse(array[i].date));

                if(array[i]==null){
                    valueset1.push(0);
                }else{
                    valueset1.push(array[i].total);
                }

                valueset2.push(Date.parse(array[i].date));
                valueset2.push(100);

                total.push(valueset1);
                expected.push(valueset2);
            }

            data.push({color: "#2E8B57", key: "TOTAL", values: total, strokeWidth: 2});
            data.push({color: "#8f65b6", key: "EXPECTED", values: expected, classed: "dashed"});

            return data;
        }

        $scope.optionsForSlackCourseMessages = {

            chart: {
                type: 'multiBarChart',
                height: 450,
                margin : {
                    top: 50,
                    right: 150,
                    bottom: 100,
                    left:100
                },

                x: function(d){ return d[0]; },
                y: function(d){ return d[1]; },

                clipEdge: true,
                duration: 500,
                stacked: false,

                xAxis: {
                    axisLabel: 'Date of Activity',
                    showMaxMin: false
                },

                yAxis: {
                    axisLabel: 'Slack Message Totals',
                    axisLabelDistance: 0
                }
            }
        };

        function getDataForSlackCourseMessages(array){

            var data = []; var total = [];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];

                valueset1.push(array[i].date);
                valueset1.push(array[i].total);

                total.push(valueset1);
            }

            data.push({color: "#2E8B57", key: "TOTAL", values: total});

            return data;
        }

        var fireRefreshEventOnWindow = function () {
            var evt = document.createEvent("HTMLEvents");
            evt.initEvent('resize', true, false);
            window.dispatchEvent(evt);
        };

    }])
    .controller('TeamController', ['$scope', '$rootScope','$location', '$routeParams', 'courseService', 'teamService', 'userService', '$http', function ($scope, $rootScope, $location, $routeParams, courseService, teamService, userService, $http) {
        $scope.teamid = $routeParams.team_id;
        $scope.loadingText = "Loading Data. Please wait";
        $scope.loading = true;
        var initial;
        var course = courseService.getCourse();
        $scope.courseid = courseService.getCourse();
        if (course == null) {
            course = "none";
        }
        //console.log("course: " + course);

        if ($scope.teamid == null) {
            $scope.teamid = "none";
        }

        $http({
            url:'/course_data',
            method:"get",
            headers: {'course':$scope.courseid}
        }).then(function (response) {

            $scope.startDate = response.data.start_date;
            var end_date = response.data.end_date
            var now = new Date();
            $scope.endDate = (end_date.value>now.value?now:end_date);
        },function (response) {
            console.log("Couldn't retrieve course data")
        });

        $http({
            url: './ag_url',
            method: "GET"
        }).then(function (response) {
            $scope.autograder_url = response.data;
            //console.log($scope.autograder_url);
        }, function (response) {
            //fail case
            console.log("didn't work: " + response.data);
        });

        $scope.commitMaxY = 0;

        //console.log("team: " + $scope.teamid);

        $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";

        $http({
            url: './check_teamaccess',
            method: "GET",
            headers: {
                'course': course,
                'team': $scope.teamid,
                'login': userService.getUser(),
                'auth': userService.getAuth()
            }
        }).then(function (response) {
            //console.log("Worked!");
            //console.log(response.data);
            if (response.data == false) {
                $location.path('/home');
            } else {
                initial = true;
                getGithubIntervals();
                }
            }, function (response) {
                    //fail case
                    console.log("didn't work");
                    $location.path('/home');
                });

        function getTaigaCourseWeightFreq() {
            $http({
                url: './taiga/course_weightFreq',
                method: "GET",
                headers: {'course': course,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                $scope.courseArrayTG = response.data;
                getGitHubCourseWeightFreq();
            }, function (response) {
                //fail case
                console.log("didn't work");
                getGitHubCourseWeightFreq();
            });
        }

        function getGitHubCourseWeightFreq() {
            $http({
                url: './github/course_weightFreq',
                method: "GET",
                headers: {'course': course,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                $scope.courseArrayGH = response.data;
                getSlackCourseWeightFreq();
            }, function (response) {
                //fail case
                console.log("didn't work");
                getSlackCourseWeightFreq();
            });
        }

        function getSlackCourseWeightFreq() {
            $http({
                url: './slack/course_weightFreq',
                method: "GET",
                headers: {'course': course,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                $scope.courseArraySK = response.data;
                getTaigaTeamWeightFreq();
            }, function (response) {
                //fail case
                console.log("didn't work");
                getTaigaTeamWeightFreq();
            });
        }

        function getTaigaTeamWeightFreq() {
            $http({
                url: './taiga/team_weightFreq',
                method: "GET",
                headers: {'course': course, 'team': $scope.teamid,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                $scope.teamArrayTG = response.data;
                getGitHubTeamWeightFreq();
            }, function (response) {
                //fail case
                console.log("didn't work");
                getGitHubTeamWeightFreq();
            });
        }

        function getGitHubTeamWeightFreq() {
            $http({
                url: './github/team_weightFreq',
                method: "GET",
                headers: {'course': course, 'team': $scope.teamid,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                $scope.teamArrayGH = response.data;
                getSlackTeamWeightFreq();
            }, function (response) {
                //fail case
                console.log("didn't work");
                getSlackTeamWeightFreq();
            });
        }

        function getSlackTeamWeightFreq() {
            $http({
                url: './slack/team_weightFreq',
                method: "GET",
                headers: {'course': course, 'team': $scope.teamid,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                $scope.teamArraySK = response.data;
                plotCurrentWeek();
            }, function (response) {
                //fail case
                console.log("didn't work");
                plotCurrentWeek();
            });
        }

        function plotCurrentWeek() {

            if($scope.courseArrayTG[0] == null){
                $scope.courseWeightTG0 = 0;
                $scope.courseFrequencyTG0 = 0;
            }else{
                $scope.courseWeightTG0 = $scope.courseArrayTG[0].weight;
                $scope.courseFrequencyTG0 = $scope.courseArrayTG[0].frequency;
            }
            if($scope.courseArrayGH[0] == null){
                $scope.courseWeightGH0 = 0;
                $scope.courseFrequencyGH0 = 0;
            }else{
                $scope.courseWeightGH0 = $scope.courseArrayGH[0].weight;
                $scope.courseFrequencyGH0 = $scope.courseArrayGH[0].frequency;
            }
            if($scope.courseArraySK[0] == null){
                $scope.courseWeightSK0 = 0;
                $scope.courseFrequencySK0 = 0;
            }else{
                $scope.courseWeightSK0 = $scope.courseArraySK[0].weight;
                $scope.courseFrequencySK0 = $scope.courseArraySK[0].frequency;
            }
            if($scope.teamArrayTG[0] == null){
                $scope.teamWeightTG0 = 0;
                $scope.teamFrequencyTG0 = 0;
            }else{
                $scope.teamWeightTG0 = $scope.teamArrayTG[0].weight;
                $scope.teamFrequencyTG0 = $scope.teamArrayTG[0].frequency;
            }
            if($scope.teamArrayGH[0] == null){
                $scope.teamWeightGH0 = 0;
                $scope.teamFrequencyGH0 = 0;
            }else{
                $scope.teamWeightGH0 = $scope.teamArrayGH[0].weight;
                $scope.teamFrequencyGH0 = $scope.teamArrayGH[0].frequency;
            }
            if($scope.teamArraySK[0] == null){
                $scope.teamWeightSK0 = 0;
                $scope.teamFrequencySK0 = 0;
            }else{
                $scope.teamWeightSK0 = $scope.teamArraySK[0].weight;
                $scope.teamFrequencySK0 = $scope.teamArraySK[0].frequency;
            }

            $scope.selectedWeekLabels = ['Taiga Impact', 'Taiga Freq', 'GH Impact', 'GH Freq', 'Slack Impact', 'Slack Freq'];
            $scope.selectedWeekOptions = { legend: { display: true }};
            $scope.selectedWeekSeries1 = "Course";
            $scope.selectedWeekSeries2 = "Team";
            $scope.selectedWeekData = [
                [$scope.courseWeightTG0, $scope.courseFrequencyTG0, $scope.courseWeightGH0, $scope.courseFrequencyGH0, $scope.courseWeightSK0, $scope.courseFrequencySK0],
                [$scope.teamWeightTG0, $scope.teamFrequencyTG0, $scope.teamWeightGH0, $scope.teamFrequencyGH0, $scope.teamWeightSK0, $scope.teamFrequencySK0]
            ];

            if(initial){
                //console.log("Initial Run for Selected Chart");
                var selectedOverallCtx = $("#radarSelected").get(0).getContext("2d");
                $scope.selectedOverallChart = new Chart(selectedOverallCtx, {
                    type: 'radar',
                    data: {
                        labels: $scope.selectedWeekLabels,
                        datasets: [
                            {
                                fill: 'origin',
                                backgroundColor:'rgb(42, 137, 232, .25)',
                                borderColor: 'rgb(42, 137, 232)',
                                borderWidth: 2,
                                label: $scope.selectedWeekSeries2,
                                data: $scope.selectedWeekData[1],
                                pointRadius: 3,
                                pointBorderWidth: 2,
                                pointBackgroundColor: 'rgb(42, 137, 232, .25)',
                                pointBorderColor: 'rgb(42, 137, 232)',
                                pointHoverRadius: 6
                            },
                            {
                                fill: 'origin',
                                backgroundColor:'rgb(228, 98, 98, .25)',
                                borderColor: 'rgb(228, 98, 98)',
                                borderWidth: 2,
                                label: $scope.selectedWeekSeries1,
                                data: $scope.selectedWeekData[0],
                                pointRadius: 3,
                                pointBorderWidth: 2,
                                pointBackgroundColor: 'rgb(228, 98, 98, .25)',
                                pointBorderColor: 'rgb(228, 98, 98)',
                                pointHoverRadius: 6
                            }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        legend: {
                            display: true
                        },
                        scale: {
                            ticks: {
                                beginAtZero: true,
                                min: 0,
                                max: 3,
                                stepSize: .5
                            },
                            pointLabels: {
                                fontSize: 14
                            }
                        }
                    }
                });
                initial = false;
                plotPreviousWeek();
            } else {
                //console.log("Not Initial Run for Selected Chart");
                $('#radarSelected').remove();
                $('#selectedOverall').append('<canvas id="radarSelected" class="chart" style="width:100%; height:100%"></canvas>');
                var selectedOverallCtx = $("#radarSelected").get(0).getContext("2d");
                $scope.selectedOverallChart = new Chart(selectedOverallCtx, {
                    type: 'radar',
                    data: {
                        labels: $scope.selectedWeekLabels,
                        datasets: [
                            {
                                fill: 'origin',
                                backgroundColor:'rgb(42, 137, 232, .25)',
                                borderColor: 'rgb(42, 137, 232)',
                                borderWidth: 2,
                                label: $scope.selectedWeekSeries2,
                                data: $scope.selectedWeekData[1],
                                pointRadius: 3,
                                pointBorderWidth: 2,
                                pointBackgroundColor: 'rgb(42, 137, 232, .25)',
                                pointBorderColor: 'rgb(42, 137, 232)',
                                pointHoverRadius: 6
                            },
                            {
                                fill: 'origin',
                                backgroundColor:'rgb(228, 98, 98, .25)',
                                borderColor: 'rgb(228, 98, 98)',
                                borderWidth: 2,
                                label: $scope.selectedWeekSeries1,
                                data: $scope.selectedWeekData[0],
                                pointRadius: 3,
                                pointBorderWidth: 2,
                                pointBackgroundColor: 'rgb(228, 98, 98, .25)',
                                pointBorderColor: 'rgb(228, 98, 98)',
                                pointHoverRadius: 6
                            }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        legend: {
                            display: true
                        },
                        scale: {
                            ticks: {
                                beginAtZero: true,
                                min: 0,
                                max: 3,
                                stepSize: .5
                            },
                            pointLabels: {
                                fontSize: 14
                            }
                        }
                    }
                });
            }
        }

        function plotPreviousWeek() {

            if($scope.courseArrayTG[1] == null){
                $scope.courseWeightTG1 = 0;
                $scope.courseFrequencyTG1 = 0;
            }else{
                $scope.courseWeightTG1 = $scope.courseArrayTG[1].weight;
                $scope.courseFrequencyTG1 = $scope.courseArrayTG[1].frequency;
            }
            if($scope.courseArrayGH[1] == null){
                $scope.courseWeightGH1 = 0;
                $scope.courseFrequencyGH1 = 0;
            }else{
                $scope.courseWeightGH1 = $scope.courseArrayGH[1].weight;
                $scope.courseFrequencyGH1 = $scope.courseArrayGH[1].frequency;
            }
            if($scope.courseArraySK[1] == null){
                $scope.courseWeightSK1 = 0;
                $scope.courseFrequencySK1 = 0;
            }else{
                $scope.courseWeightSK1 = $scope.courseArraySK[1].weight;
                $scope.courseFrequencySK1 = $scope.courseArraySK[1].frequency;
            }
            if($scope.teamArrayTG[1] == null){
                $scope.teamWeightTG1 = 0;
                $scope.teamFrequencyTG1 = 0;
            }else{
                $scope.teamWeightTG1 = $scope.teamArrayTG[1].weight;
                $scope.teamFrequencyTG1 = $scope.teamArrayTG[1].frequency;
            }
            if($scope.teamArrayGH[1] == null){
                $scope.teamWeightGH1 = 0;
                $scope.teamFrequencyGH1 = 0;
            }else{
                $scope.teamWeightGH1 = $scope.teamArrayGH[1].weight;
                $scope.teamFrequencyGH1 = $scope.teamArrayGH[1].frequency;
            }
            if($scope.teamArraySK[1] == null){
                $scope.teamWeightSK1 = 0;
                $scope.teamFrequencySK1 = 0;
            }else{
                $scope.teamWeightSK1 = $scope.teamArraySK[1].weight;
                $scope.teamFrequencySK1 = $scope.teamArraySK[1].frequency;
            }


            $scope.overallWeekLabels = ['Taiga Impact', 'Taiga Freq', 'GH Impact', 'GH Freq', 'Slack Impact', 'Slack Freq'];
            $scope.overallWeekSeries1 = "Course";
            $scope.overallWeekSeries2 = "Team";
            $scope.overallWeekData = [
                [$scope.courseWeightTG1, $scope.courseFrequencyTG1, $scope.courseWeightGH1, $scope.courseFrequencyGH1, $scope.courseWeightSK1, $scope.courseFrequencySK1],
                [$scope.teamWeightTG1, $scope.teamFrequencyTG1, $scope.teamWeightGH1, $scope.teamFrequencyGH1, $scope.teamWeightSK1, $scope.teamFrequencySK1]
            ];

            var totalOverallCtx = $("#radarOverall").get(0).getContext("2d");
            $scope.totalOverallChart = new Chart(totalOverallCtx, {
                type: 'radar',
                data: {
                    labels: $scope.overallWeekLabels,
                    datasets: [
                        {
                            fill: 'origin',
                            backgroundColor:'rgb(42, 137, 232, .25)',
                            borderColor: 'rgb(42, 137, 232)',
                            borderWidth: 2,
                            label: $scope.overallWeekSeries2,
                            data: $scope.overallWeekData[1],
                            pointRadius: 3,
                            pointBorderWidth: 2,
                            pointBackgroundColor: 'rgb(42, 137, 232, .25)',
                            pointBorderColor: 'rgb(42, 137, 232)',
                            pointHoverRadius: 6
                        },
                        {
                            fill: 'origin',
                            backgroundColor:'rgb(228, 98, 98, .25)',
                            borderColor: 'rgb(228, 98, 98)',
                            borderWidth: 2,
                            label: $scope.overallWeekSeries1,
                            data: $scope.overallWeekData[0],
                            pointRadius: 3,
                            pointBorderWidth: 2,
                            pointBackgroundColor: 'rgb(228, 98, 98, .25)',
                            pointBorderColor: 'rgb(228, 98, 98)',
                            pointHoverRadius: 6
                        }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    legend: {
                        display: true
                    },
                    scale: {
                        ticks: {
                            beginAtZero: true,
                            min: 0,
                            max: 3,
                            stepSize: .5
                        },
                        pointLabels: {
                            fontSize: 14
                        }
                    }
                }
            });
        }


        function getDataForTeamTaigaActivityCharts(array){

            var data = []; var inProgress = []; var toTest = []; var done = [];var expected = [];var maxArr=[];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];var valueset2 = [];var valueset3 = [];var valueset4 = [];

                valueset1.push(array[i].rawWeekEnding*1000);
                valueset1.push(array[i].inProgressActivity);

                valueset2.push(array[i].rawWeekEnding*1000);
                valueset2.push(array[i].toTestActivity);

                valueset3.push(array[i].rawWeekEnding*1000);
                valueset3.push(array[i].doneActivity);

                valueset4.push(array[i].rawWeekEnding*1000);
                valueset4.push(3);

                inProgress.push(valueset1);
                toTest.push(valueset2);
                done.push(valueset3);
                expected.push(valueset4);
				maxArr.push(array[i].inProgressActivity);
                maxArr.push(array[i].toTestActivity);
                maxArr.push(array[i].doneActivity);
            }
			var taigaLineChartMax = Math.ceil(Math.max.apply(Math, maxArr));
            $scope.taigaMaxY = getTaigaLineChartMax(taigaLineChartMax);

            data.push({color: "#6799ee", key: "IN PROGRESS", values: inProgress, strokeWidth: 2});
            data.push({color: "#000000", key: "READY FOR TEST", values: toTest, strokeWidth: 2});
            data.push({color: "#2E8B57", key: "CLOSED", values: done, strokeWidth: 2});
            data.push({color: "#8f65b6", key: "EXPECTED", values: expected, classed: "dashed"});
            return data;
        }
		function getTaigaLineChartMax(taigaLineChartMax){
            if(taigaLineChartMax > 50){
                return 50;
            } else {
                return taigaLineChartMax;
            }
        }
        function getGithubIntervals() {
            $http({
                url: './github/team_intervals',
                method: "GET",
                headers: {'course': course, 'team': $scope.teamid}
            }).then(function (response) {
                //console.log("Worked!");
                //console.log(response.data);
                $scope.teamIntervals = response.data;


                console.log("Start: " + $rootScope.Period.start);
                console.log("End: " + $rootScope.Period.end);
                if ($rootScope.Period.start == null && $rootScope.Period.end == null) {
                    $rootScope.Period.start = new Date($scope.courseIntervals[$scope.courseIntervals.length - 1].weekBeginning);
                    $rootScope.Period.end = new Date($scope.courseIntervals[$scope.courseIntervals.length - 1].weekEnding);
                    if($rootScope.Period.start.valueOf()<Date.parse($scope.startDate))
                        $rootScope.Period.start = new Date(Date.parse($scope.startDate));
                    console.log("initial end: "+$rootScope.Period.end.value);
                    if($rootScope.Period.end.valueOf()>Date.parse($scope.endDate))
                        $rootScope.Period.end = new Date(Date.parse($scope.endDate));
                    $scope.IntervalChangedEnd();
                }
                else {
                    $scope.IntervalChangedEnd();
                }
                
                /*if($rootScope.rawWeekBeginning == null && $rootScope.rawWeekEnding == null) {
                    $scope.SelectedWeekBeginning = $scope.teamIntervals[$scope.teamIntervals.length-1];
                    $rootScope.rawWeekBeginning = $scope.SelectedWeekBeginning.weekBeginning;
                    $scope.SelectedWeekEnding =  $scope.teamIntervals[$scope.teamIntervals.length-1];
                    $scope.IntervalChangedEnd($scope.SelectedWeekEnding.weekEnding);
                }
                else {
                    $scope.SelectedWeekBeginning.weekBeginning = $rootScope.rawWeekBeginning/!*$scope.teamIntervals.find(function(element) {
                        return element.rawWeekBeginning == $rootScope.rawWeekBeginning;
                    });*!/
                    $scope.SelectedWeekEnding.weekEnding = $rootScope.rawWeekEnding/!*$scope.teamIntervals.find(function(element) {
                        return element.rawWeekEnding == $rootScope.rawWeekEnding;
                    });*!/
                    $scope.IntervalChangedEnd($rootScope.rawWeekEnding);
                }*/
                $('select option')
                    .filter(function() {
                        return !this.value || $.trim(this.value).length == 0 || $.trim(this.text).length == 0;
                    })
                    .remove();
            }, function (response) {
                //fail case
                console.log("didn't work");
                $('select option')
                    .filter(function() {
                        return !this.value || $.trim(this.value).length == 0 || $.trim(this.text).length == 0;
                    })
                    .remove();
            });
        }
        $scope.IntervalChangedBegin = function () {
            $scope.loading = true;
            console.log("WeekBeginning: " + $rootScope.Period.start);
            if ($rootScope.Period.end != null) {
                if($rootScope.Period.end<$rootScope.Period.start){
                    $rootScope.Period.end = $rootScope.Period.start;
                    $rootScope.Period.end.setHours(23,59,59,999);
                }
                $rootScope.Period.start.setHours(0,0,0,0);
                getAllTeamTabsData();
            }
        };
        $scope.IntervalChangedEnd = function () {
            $scope.loading = true;
            console.log("WeekEnding: " + $rootScope.Period.end);
            if ($rootScope.Period.start != null) {
                if($rootScope.Period.start>$rootScope.Period.end){
                    $rootScope.Period.start = $rootScope.Period.end;
                    $rootScope.Period.start.setHours(0,0,0,0);
                }
                $rootScope.Period.end.setHours(23,59,59,999);
               getAllTeamTabsData();
            }
        };
        function getAllTeamTabsData(){
            getTaigaCourseWeightFreq();
            $http({
                url: './taiga/team_tasks',
                method: "GET",
                headers: {
                    'course': course,
                    'team': $scope.teamid,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                //console.log("Worked!");
                //console.log(response.data);
                $scope.teamTasks = response.data;
                $scope.dataForTaigaTeamTasks = getDataForTaigaTeamTasks(response.data);
            });
            getTaigaActivity();
            $http({
                url: './github/commits_team',
                method: "GET",
                headers: {
                    'course': course, 'team': $scope.teamid,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                // console.log("Worked This is what the GitHub Data is showing: !");
                //console.log(response.data);
                processGitHubCommitMax(response.data);
            }, function (response) {
                //fail case
                console.log("didn't work");
            });
            $http({
                url: './slack/team_messages',
                method: "GET",
                headers: {
                    'course': course,
                    'team': $scope.teamid,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                //console.log("SlackTeamMessages");
                //console.log(response.data);
                $scope.teamTasks = response.data;
                $scope.dataForSlackTeamMessages = getDataForSlackTeamMessages(response.data);
            });
            getSlackActivity();
        }
        $scope.optionsForTaigaTeamTasks = {

            chart: {
                type: 'multiBarChart',
                height: 450,
                margin : {
                    top: 50,
                    right: 150,
                    bottom: 100,
                    left:100
                },

                x: function(d){ return d[0]; },
                y: function(d){ return d[1]; },

                clipEdge: true,
                duration: 500,
                stacked: false,

                xAxis: {
                    axisLabel: 'Date of Activity',
                    showMaxMin: false
                },

                yAxis: {
                    axisLabel: 'Taiga Task Totals',
                    axisLabelDistance: 0
                }
            }
        };

        function getDataForTaigaTeamTasks(array){

            var data = []; var inProgress = []; var toTest = []; var done =[];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];var valueset2 = [];var valueset3 = [];

                valueset1.push(array[i].date);
                valueset1.push(array[i].inProgress);

                valueset2.push(array[i].date);
                valueset2.push(array[i].toTest);

                valueset3.push(array[i].date);
                valueset3.push(array[i].done);

                inProgress.push(valueset1);
                toTest.push(valueset2);
                done.push(valueset3);
            }

            data.push({color: "#6799ee", key: "IN PROGRESS", values: inProgress});
            data.push({color: "#000000", key: "READY FOR TEST", values: toTest});
            data.push({color: "#2E8B57", key: "CLOSED", values: done});

            return data;
        }

        function getTaigaActivity() {
            $scope.taigaMaxY = 0;
            $http({
                url: './taiga/team_activity',
                method: "GET",
                headers: {'course': course, 'team': $scope.teamid}
            }).then(function (response) {
                //console.log("Worked!");
                //console.log(response.data);
                $scope.teamActivity = response.data;
                $scope.dataForTaigaTeamActivity = getDataForTeamTaigaActivityCharts(response.data);
            }, function (response) {
                //fail case
                console.log("didn't work: " + response.data);
            });
        }

        $scope.optionsForTaigaTeamActivity = {

            chart: {
                type: 'lineChart',
                height: 450,
                margin : {
                    top: 50,
                    right: 150,
                    bottom: 100,
                    left:100
                },

                x: function(d){ return d[0]; },
                y: function(d){ return d[1]; },

                useInteractiveGuideline: true,

                xAxis: {
                    axisLabel: 'Date of Activity',
                    tickFormat: function(d) {
                        return d3.time.format('%Y-%m-%d')(new Date(d))
                    },

                    showMaxMin: false,
                    staggerLabels: false
                },

                yAxis: {
                    axisLabel: 'Taiga Task Activity',

                    axisLabelDistance: 0
                    //,tickValues:  [0, 5, 10, 15, 20, 25, 30, 35, 40]
                },

                yDomain:[0, $scope.taigaMaxY]

            }
        };

        function processGitHubCommitMax(array){
            var commits = []; var linesOfCodeAdded = []; var linesOfCodeDeleted = [];
            for (var i = 0; i < array.length; i++){
                commits.push(array[i].commits);
                linesOfCodeAdded.push(array[i].linesOfCodeAdded/100);
                linesOfCodeDeleted.push(array[i].linesOfCodeDeleted/100);
            }
            var maxArray = [Math.max.apply(Math, commits), Math.max.apply(Math, linesOfCodeAdded), Math.max.apply(Math, linesOfCodeDeleted)];
            var gitHubBarChartMax = Math.ceil(Math.max.apply(Math, maxArray));
            $scope.commitMaxY = getGitHubBarChartMax(gitHubBarChartMax);
            $scope.dataForGitHubTeamCommits =  getDataForGitHubTeamCommitsCharts(array);

            commitOptions();
            getGitHubWeightData();
            listGetDetailedGithubActivityURL();
        }

        function getGitHubBarChartMax(gitHubBarChartMax){

            if(gitHubBarChartMax > 50){
                return 50;
            } else {
                return gitHubBarChartMax;
            }
        }

        function commitOptions() {
            $scope.optionsForGitHubTeamCommits = {

                chart: {
                    type: 'multiBarChart',
                    height: 450,
                    margin: {
                        top: 50,
                        right: 150,
                        bottom: 100,
                        left: 100
                    },

                    legend: {
                        margin: {
                            top: 0,
                            right: 0,
                            bottom: 20,
                            left: 0
                        },
                        maxKeyLength: 100
                    },

                    x: function (d) {
                        return d[0];
                    },
                    y: function (d) {
                        return d[1];
                    },

                    clipEdge: true,
                    duration: 500,
                    stacked: false,

                    xAxis: {
                        axisLabel: 'Date of Activity',
                        showMaxMin: false
                    },

                    yAxis: {
                        axisLabel: 'GitHub Commit Counts',
                        axisLabelDistance: 0
                    },

                    yDomain: [0, $scope.commitMaxY],
                    //callback function to drilldown to the detailed Github Activity
                    callback: function(chart) {
                        chart.multibar.dispatch.on('elementClick', function(e){
                            //  var date = e.data.toString().substring(0,10);
                            console.log('$scope.githubStartDate '+$scope.githubStartDate);
                            //listGetDetailedGithubActivityURL($scope.githubStartDate,$scope.githubEndDate);
                            listGetDetailedGithubActivityURL();
                            //getGitHubWeightStudentData($scope.githubStartDate,$scope.githubEndDate);
                        });
                    }
                }
            };
        }
        function listGetDetailedGithubActivityURL(){
            $http({
                url: './github/daily_activity_json',
                method: "GET",
                headers: {
                    'course': course,
                    'team': $scope.teamid,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                console.log(response.data);
                $scope.loading = false;
                $scope.showZeros = true;
                processGitHubCommitTotals(response.data);
            }, function (response) {
                //fail case
                   console.log("didn't work");
            });
        }

        function getGitHubCommitSummary(array)
        {
            var githubSummary = new Map();
            for (var i = 0; i < array.length; i++)
            {
                var outerMap = new Map();
                for (var j = 0; j < array[i].daily_activity.length; j++)
                {
                    for(var k = 0; k < array[i].daily_activity[j].commit_details.length; k++)
                    {
                        var branch = array[i].daily_activity[j].commit_details[k].branch;
                        if(outerMap.has(branch))
                        {
                            var temp = outerMap.get(branch);
                            temp.set("commits",temp.get("commits") + 1);
                            temp.set("additions",temp.get("additions") + array[i].daily_activity[j].commit_details[k].additions);
                            temp.set("deletions",temp.get("deletions") + array[i].daily_activity[j].commit_details[k].deletions);
                            temp.set("total",temp.get("total") + array[i].daily_activity[j].commit_details[k].total);
                            outerMap.set(branch,temp);
                        }
                        else
                        {
                            var innerMap = new Map();
                            innerMap.set("commits",1);
                            innerMap.set("additions",array[i].daily_activity[j].commit_details[k].additions);
                            innerMap.set("deletions",array[i].daily_activity[j].commit_details[k].deletions);
                            innerMap.set("total",array[i].daily_activity[j].commit_details[k].total);
                            outerMap.set(branch,innerMap);
                        }

                    }
                }//j loop

                var student_name = "";
                if(array[i].name!=null) {
                    student_name = array[i].name;
                }
                else if(array[i].userid!=null) {
                    student_name = array[i].userid;
                }

                githubSummary.set(student_name,outerMap);

            }//i loop

            return githubSummary;
        }


        function processGitHubCommitTotals(array){



            var commits = []; var linesOfCodeAdded = []; var linesOfCodeDeleted = []; var totals = [];
            for (var i = 0; i < array.length; i++){

                var gitHubSummary = getGitHubCommitSummary(array);

                gitHubSummary.forEach(function(valueStudentMap, keyStudentMap) {
                    valueStudentMap.forEach(function(valueBranchMap, keyBranchMap) {
                        commits.push(valueBranchMap.get("commits"));
                        linesOfCodeAdded.push(valueBranchMap.get("additions"));
                        linesOfCodeDeleted.push(valueBranchMap.get("deletions"));
                        totals.push(valueBranchMap.get("total")/1000);

                    });
                });

            }
            var maxArray = [Math.max.apply(Math, commits), Math.max.apply(Math, linesOfCodeAdded), Math.max.apply(Math, linesOfCodeDeleted),Math.max.apply(Math, totals)];
            var gitHubBarChartMax = Math.ceil(Math.max.apply(Math, maxArray));
            $scope.commitMaxY = getGitHubBarChartMax(gitHubBarChartMax);

            /**
             * Temporarily commenting out the sub-charts for GitHub data  from the AutoGrader Tool
             * TODO: Re-implement sub-charting data below following Taiga AG integration and getting both AG tools on server
             */
            console.log("array here")
            console.log(array);

            $scope.dataForGitHubTeamTotals =  getDataForGitHubTeamCommitsSubCharts(array);
            $scope.dataForGitHubTeamTotalsDaily = getDataForGitHubTeamTotalsDaily(array);
            commitTotals();
        }

        function commitTotals() {

            $scope.optionsForGitHubTeamTotals = {

              chart: {  type: 'multiBarChart',
                height: 450,
                margin: {
                    top: 50,
                    right: 150,
                    bottom: 100,
                    left: 100
                },

                legend: {
                    margin: {
                        top: 0,
                        right: 0,
                        bottom: 20,
                        left: 0
                    },
                        maxKeyLength: 100
                    },

                    x: function (d) {
                        return d[0];
                    },
                    y: function (d) {
                        return d[1];
                    },

                    clipEdge: true,
                    duration: 500,
                    stacked: false,

                    xAxis: {
                        axisLabel: 'Student',
                        showMaxMin: false
                    },

                    yAxis: {
                        axisLabel: 'GitHub Commit Totals',
                        axisLabelDistance: 0
                    },

                    yDomain: [0, $scope.commitMaxY]
                }
            };
        }



        function getDataForGitHubTeamTotalsDaily(array) {

            var dataDaily = [];
            var gitHubSummary = getGitHubCommitSummary(array);
            var student_name;
            var daysOfWeek = [
                "Sun","Mon","Tues","Wed","Thur","Fri","Sat"
            ];
            for (var i = 0; i < array.length; i++){

                if(array[i].name!=null) {
                    student_name = array[i].name;
                }
                else if(array[i].userid!=null) {
                    student_name = array[i].userid;
                }

                // Start - Getting Daily activity log
                var commits = []; //commits per day
                var add = []; //additions per day
                var del = []; // deletions per day
                var total = []; // total changes per day
                var day = []; //date
                var total_color = [];//change text color based on total lines count commit


                //There can be multiple commits on a day, the below captures individual commit details for a day
                var message = []; // message for a single commit
                var html_url = []; // html url for a single commit
                var branch = []; //branch details for a single commit
                var add_commit = []; // additions of lines for an individual commit
                var del_commit = []; // deletion of lines for an individual commit
                var total_commit = []; // total changes of lines for an individual commit
                var total_active_days = 0;
                var dailyActivityLen = array[i].daily_activity.length;
                var prevDate = new Date($rootScope.Period.start);


                for(var j=0;j<dailyActivityLen;j++)
                {

                    commits.push(array[i].daily_activity[j].commits);
                    add.push(array[i].daily_activity[j].additions);
                    del.push(array[i].daily_activity[j].deletions);
                    total.push(array[i].daily_activity[j].total);

                    var currentDate = new Date(prevDate);
                    prevDate.setDate(prevDate.getDate() + 1);
                    if(array[i].daily_activity[j].commit_details.length==0)
                    {

                        html_url.push(null);
                        message.push(null);
                        branch.push(null);

                        add_commit.push(null);
                        del_commit.push(null);
                        total_commit.push(null);
                    }
                    else {

                        var html_url_arr = [];
                        var message_arr = [];
                        var branch_arr = [];
                        var addition_arr = [];
                        var deletions_arr = [];
                        var total_arr = [];
                        for(var k=0; k<array[i].daily_activity[j].commit_details.length;k++)
                        {
                            html_url_arr.push(array[i].daily_activity[j].commit_details[k].html_url);
                            message_arr.push(array[i].daily_activity[j].commit_details[k].message);
                            branch_arr.push(array[i].daily_activity[j].commit_details[k].branch);
                            addition_arr.push(array[i].daily_activity[j].commit_details[k].additions);
                            deletions_arr.push(array[i].daily_activity[j].commit_details[k].deletions);
                            total_arr.push(array[i].daily_activity[j].commit_details[k].total);
                        }
                        html_url.push(html_url_arr);
                        message.push(message_arr);
                        branch.push(branch_arr);
                        add_commit.push(addition_arr);
                        total_active_days = total_active_days + 1;
                        del_commit.push(deletions_arr);
                        total_commit.push(total_arr);
                    }
                    var mm = currentDate.getMonth() + 1;
                    if(mm<10) {
                        mm = "0" + (mm);
                    }

                    var dd = currentDate.getUTCDate();
                    if(dd<10) {
                        dd = "0" + (dd);
                    }

                    day.push(mm + "/" + dd + "/" + currentDate.getFullYear() + " (" + daysOfWeek[currentDate.getUTCDay()] + ") ");

                }

                // End - Getting Daily activity log




                //total_color
                var total_sum = Math.max.apply(null, total);
                for (var j = 0; j < total.length; j++) {
                    if(total[j]/total_sum > 0.5)
                    {
                        total_color.push(1);
                    }
                    else
                    {
                        total_color.push(0);
                    }
                }

                var other_activity = [];
                var master_activity = [];
                var inactivity_streak = [];


                var inactivity = array[i].inactivity_streaks;
                var inactivity_max = Math.max.apply(null, inactivity);
                var inactivity_sum = inactivity.reduce(function(a, b) { return a + b; }, 0);

                inactivity_streak.push({inactivity : inactivity, max : inactivity_max, sum : inactivity_sum});

                var valueStudentMap = gitHubSummary.get(student_name);
                valueStudentMap.forEach(function(valueBranchMap, keyBranchMap) {

                    if(keyBranchMap == "master")
                    {
                        master_activity.push({commits:valueBranchMap.get("commits") , additions:valueBranchMap.get("additions"), deletions:valueBranchMap.get("deletions") , total:valueBranchMap.get("total") });
                    }
                    else
                    {
                        other_activity.push({branchNameStudent:keyBranchMap, commitStudent:valueBranchMap.get("commits"), addedStudent:valueBranchMap.get("additions"),
                            deletedStudent:valueBranchMap.get("deletions"), totalStudent:valueBranchMap.get("total")})
                    }
                });


                //getting activity streak
                var activity_streak = getActivityStreak(commits,day);
                var activity_max = Math.max.apply(null, activity_streak);

                dataDaily.push({total_color: total_color, student: student_name, add_commit:add_commit, del_commit:del_commit, total_commit:total_commit, message:message, html_url:html_url, branch:branch, commits: commits, add: add, del: del, total: total, day: day, other_activity:other_activity, master_activity:master_activity, inactivity_streak:inactivity_streak, total_active_days:total_active_days, activity_streak:activity_streak, activity_max:activity_max});
            }

            return dataDaily;
        }

        function getActivityStreak(commits,day){
            var activity_streak = [];
            var streak = 0;
            var prev_date = null;
            for (var i = 0; i < commits.length; i++) {
                console.log(day[i]);
                if(commits[i]!=0)
                {
                    if(prev_date==null)
                    {
                        var parts = day[i].split('/');
                        prev_date = new Date(parts[2].substring(0, 4), parts[0] - 1, parts[1]);
                        streak = streak + 1;
                    }
                    else
                    {
                        var parts = day[i].split('/');
                        var current_date = new Date(parts[2].substring(0, 4), parts[0] - 1, parts[1]);
                        var timeDiff = Math.abs(current_date.getTime() - prev_date.getTime());
                        var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));
                        if(diffDays<=1)
                        {
                            prev_date = current_date;
                            streak = streak + 1;
                        }
                        else
                        {
                            activity_streak.push(streak);
                            prev_date = current_date;
                            streak = 1;
                        }
                    }
                }
            }
            activity_streak.push(streak);

            return activity_streak;
        }

        function getDataForGitHubTeamCommitsSubCharts(array){
            var commits = []; var linesOfCodeAdded = []; var linesOfCodeDeleted = []; var data = [];
            var totals = []; var student_name;
            var gitHubSummary = getGitHubCommitSummary(array);
            for (var i = 0; i < array.length; i++){

                var commitStudent = 0;
                var addedStudent = 0;
                var deletedStudent = 0;
                var totalStudent = 0;


                var valueset1 = [];var valueset2 = [];var valueset3 = [];var valueset4 = [];

                if(array[i].name!=null) {
                    student_name = array[i].name;
                }
                else if(array[i].userid!=null) {
                    student_name = array[i].userid;
                }

                var valueStudentMap = gitHubSummary.get(student_name);
                valueStudentMap.forEach(function(valueBranchMap, keyBranchMap) {
                    commitStudent = commitStudent + valueBranchMap.get("commits");
                    addedStudent = addedStudent + valueBranchMap.get("additions");
                    deletedStudent = deletedStudent + valueBranchMap.get("deletions");
                    totalStudent =  totalStudent + valueBranchMap.get("total");

                });


                valueset1.push(student_name);
                valueset1.push(commitStudent);

                valueset2.push(student_name);
                valueset2.push(addedStudent/1000);

                valueset3.push(student_name);
                valueset3.push(deletedStudent/1000);

                valueset4.push(student_name);
                valueset4.push(totalStudent/1000);
                commits.push(valueset1);
                linesOfCodeAdded.push(valueset2);
                linesOfCodeDeleted.push(valueset3);
                totals.push(valueset4);
            }
            data.push({color: "#6799ee", key: "Commits", values: commits});
            data.push({color: "#000000", key: "Lines Of Code Added/1000", values: linesOfCodeAdded});
            data.push({color: "#2E857E", key: "Lines Of Code Deleted/1000", values: linesOfCodeDeleted});
            data.push({color: "#900C3F", key: "Totals/1000", values: totals});


            return data;
        }

        ////* Function to Parse GitHub CommitData for MultiBar Chart * ////

        function getDataForGitHubTeamCommitsCharts(array){

            var commits = []; var linesOfCodeAdded = []; var linesOfCodeDeleted = []; var data = [];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];var valueset2 = [];var valueset3 = [];

                valueset1.push(array[i].gitHubPK.date);
                valueset1.push(array[i].commits);

                valueset2.push(array[i].gitHubPK.date);
                valueset2.push(array[i].linesOfCodeAdded/100);

                valueset3.push(array[i].gitHubPK.date);
                valueset3.push(array[i].linesOfCodeDeleted/100);

                commits.push(valueset1);
                linesOfCodeAdded.push(valueset2);
                linesOfCodeDeleted.push(valueset3);
            }
            try
            {
                $scope.githubStartDate = array[0].gitHubPK.date;
                $scope.githubEndDate = array[array.length-1].gitHubPK.date;
                $scope.loadingText = "Loading Data. Please wait";
            }
            catch(err)
            {
                $scope.loadingText = "Cannot fetch data for the following team";
            }
            data.push({color: "#6799ee", key: "Commits", values: commits});
            data.push({color: "#000000", key: "Lines Of Code Added/100", values: linesOfCodeAdded});
            data.push({color: "#2E8B57", key: "Lines Of Code Deleted/100", values: linesOfCodeDeleted});
            return data;
        }

        function getGitHubWeightData() {
            $http({
                url: './github/weights_team',
                method: "GET",
                headers: {'course': course, 'team': $scope.teamid}
            }).then(function (response) {
                //console.log("Worked This is what the GitHub Weight is showing: !");
                //console.log(response.data);
                $scope.dataForGitHubTeamWeight= getDataForGitHubTeamWeightCharts(response.data);
                getSlackActivity();
               // getGitHubWeightStudentData();
            }, function (response) {
                //fail case
                console.log("didn't work");
                getSlackActivity();
                //getGitHubWeightStudentData();
            });
        }

        $scope.optionsForGitHubTeamWeight = {

            chart: {
                type: 'lineChart',
                height: 450,
                margin: {
                    top: 50,
                    right: 150,
                    bottom: 100,
                    left: 100
                },

                x: function (d) {
                    return d[0];
                },
                y: function (d) {
                    return d[1];
                },

                useInteractiveGuideline: true,

                xAxis: {
                    axisLabel: 'Date of Activity',
                    tickFormat: function (d) {
                        return d3.time.format('%Y-%m-%d')(new Date(d))
                    },

                    showMaxMin: false,
                    staggerLabels: false
                },

                yAxis: {
                    axisLabel: 'GitHub Task Updates Weight',
                    axisLabelDistance: 0,
                    tickValues: [0, 3, 6, 9, 12, 15]
                },

                yDomain: [0, 15]
                        }
                    }

        ////* Function to Parse GitHub Weight for Line Chart * ////

        function getDataForGitHubTeamWeightCharts(array){

            var weight = []; var expected = []; var data = [];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];var valueset2 = [];

                valueset1.push(Date.parse(array[i].gitHubPK.date));
                valueset1.push(array[i].weight);

                valueset2.push(Date.parse(array[i].gitHubPK.date));
                valueset2.push(4);

                weight.push(valueset1);
                expected.push(valueset2);
            }
            //$scope.githubDailyStartDate = d3.time.format('%Y-%m-%d')(new Date(valueset1[0]));
            //$scope.githubDailyEndDate = d3.time.format('%Y-%m-%d')(new Date(valueset1[valueset1.length-2]));
            data.push({color: "#6799ee", key: "Weight", values: weight, strokeWidth: 2});
            data.push({color: "#000000", key: "Expected", values: expected, strokeWidth: 2});

            return data;
        }
//-----------------------
        function getGitHubWeightStudentData(startDate,endDate) {
            console.log('weekBeginning'+startDate,'weekEnding'+endDate);
            $http({
                url: './github/daily_activity_json',
                method: "GET",
                headers: {'course': course,'team': $scope.teamid, 'weekBeginning':startDate.valueOf(),'weekEnding':endDate.valueOf()}
            }).then(function (response) {
                $scope.dataForGitHubTeamSubWeight= getDataForGitHubTeamSubWeightCharts(response.data);
                getSlackActivity();
            }, function (response) {
                //fail case
                console.log("didn't work");
                getSlackActivity();
            });

        }

        $scope.optionsForGitHubTeamSubWeight = {

            chart: {
                type: 'lineChart',
                height: 450,
                margin : {
                    top: 50,
                    right: 150,
                    bottom: 100,
                    left:100
                },

                x: function(d){ return d[0]; },
                y: function(d){ return d[1]; },

                useInteractiveGuideline: true,

                xAxis: {
                    axisLabel: 'Date of Activity',
                    tickFormat: function(d) {
                        return d3.time.format('%Y-%m-%d')(new Date(d))
                    },

                    showMaxMin: false,
                    staggerLabels: false
                },

                yAxis: {
                    axisLabel: 'GitHub Task Updates Weight',
                    axisLabelDistance: 0,
                    tickValues:  [0, 3, 6, 9, 12, 15]
                },

                yDomain:[0, 15],
                 }
        };

        ////* Function to Parse GitHub Weight for Line Chart * ////

        function getDataForGitHubTeamSubWeightCharts(array){

            var weight = []; var expected = []; var data = [];
            var colors = ["#6799ee","#000000","#888888","#010101"];
            var name=[];
                var valueset1 = [];var valueset2 = [];
                for(var j=0;j<array[0].daily_activity.length;j++){
                    valueset1.push(j);
                    valueset1.push(array[0].daily_activity[j].commits);
                }
                weight.push(valueset1);
                name.push(array[0].name);
            for(var j=0;j<array[1].daily_activity.length;j++){
                valueset2.push(j);
                valueset2.push(array[1].daily_activity[j].commits);
            }
            expected.push(valueset2);
            name.push(array[1].name);
            data.push({color: colors[0], key: name[0], values: weight, strokeWidth: 2});
            data.push({color: colors[1], key: name[1], values: expected, strokeWidth: 2});
            return data;}

        //----------------
        function getSlackActivity() {
            $http({
                url: './slack/team_totals',
                method: "GET",
                headers: {'course': course, 'team': $scope.teamid}
            }).then(function (response) {
                //console.log("SlackActivity");
                //console.log(response.data);
                $scope.slackTeamActivity = response.data;
                $scope.dataForSlackTeamActivity = getDataForTeamSlackActivityCharts(response.data);
                }, function (response) {
                //fail case
                console.log("didn't work");
                 });
        }

        $scope.optionsForSlackTeamActivity = {

            chart: {
                type: 'lineChart',
                height: 450,
                margin : {
                    top: 50,
                    right: 150,
                    bottom: 100,
                    left:100
                },

                x: function(d){ return d[0]; },
                y: function(d){ return d[1]; },

                useInteractiveGuideline: true,

                xAxis: {
                    axisLabel: 'Date of Activity',
                    tickFormat: function(d) {
                        return d3.time.format('%Y-%m-%d')(new Date(d))
                    },

                    showMaxMin: false,
                    staggerLabels: false
                },

                yAxis: {
                    axisLabel: 'Slack Message Activity',
                    axisLabelDistance: 0,
                    tickValues:  [0, 25, 50, 100, 125, 125, 175, 200]
                },

                yDomain:[0, 200]

            }
        };

        function getDataForTeamSlackActivityCharts(array){

            var data = []; var total = [];var expected = [];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];var valueset2 = []

                valueset1.push(Date.parse(array[i].date));
                if(array[i]==null){
                    valueset1.push(0);
                }else{
                    valueset1.push(array[i].total);
                }

                valueset2.push(Date.parse(array[i].date));
                valueset2.push(100);

                total.push(valueset1);
                expected.push(valueset2);
            }

            data.push({color: "#2E8B57", key: "TOTAL", values: total, strokeWidth: 2});
            data.push({color: "#8f65b6", key: "EXPECTED", values: expected, classed: "dashed"});

            return data;
        }

        $scope.optionsForSlackTeamMessages = {

            chart: {
                type: 'multiBarChart',
                height: 450,
                margin : {
                    top: 50,
                    right: 150,
                    bottom: 100,
                    left:100
                },

                x: function(d){ return d[0]; },
                y: function(d){ return d[1]; },

                clipEdge: true,
                duration: 500,
                stacked: false,

                xAxis: {
                    axisLabel: 'Date of Activity',
                    showMaxMin: false
                },

                yAxis: {
                    axisLabel: 'Slack Message Totals',
                    axisLabelDistance: 0
                }
            }
        };

        function getDataForSlackTeamMessages(array){

            var data = []; var total = [];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];

                valueset1.push(array[i].date);
                valueset1.push(array[i].total);

               total.push(valueset1);
            }

            data.push({color: "#2E8B57", key: "TOTAL", values: total});

            return data;
        }

        var fireRefreshEventOnWindow = function () {
            var evt = document.createEvent("HTMLEvents");
            evt.initEvent('resize', true, false);
            window.dispatchEvent(evt);
        };

    }])
    .controller('StudentController', ['$filter', '$rootScope','$scope', '$location', '$routeParams', 'courseService', 'teamService', 'studentService', 'userService', '$http', function ($filter, $rootScope, $scope, $location, $routeParams, courseService, teamService, studentService, userService, $http) {
        $scope.studentid = $routeParams.student_id;
        $scope.courseid = courseService.getCourse();
        $scope.teamid = teamService.getTeam();
        var initial = true;
		var course = courseService.getCourse();
        var team = teamService.getTeam();
        var studentemail = studentService.getStudentEmail();

        if (course == null) {
            course = "none";
            //console.log("course: " + course);
        }

        if (team == null) {
            team = "none";
            //console.log("team: " + team);
        }

        if (studentemail == null) {
            studentemail = "none";
            //console.log("studentemail: " + studentemail);
        }

        $http({
            url:'/course_data',
            method:"get",
            headers: {'course':$scope.courseid}
        }).then(function (response) {

            $scope.startDate = response.data.start_date;
            var end_date = response.data.end_date
            var now = new Date();
            $scope.endDate = (end_date.value>now.value?now:end_date);
        },function (response) {
            console.log("Couldn't retrieve course data")
        });

        $http({
            url: './ag_url',
            method: "GET"
        }).then(function (response) {
            $scope.autograder_url = response.data;
            //console.log($scope.autograder_url);
        }, function (response) {
            //fail case
            console.log("didn't work: " + response.data);
        });

        $scope.commitMaxY = 0;

        $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";
        $http({
            url: './check_studentaccess',
            method: "GET",
            headers: {
                'course': course,
                'team': team,
                'login': userService.getUser(),
                'auth': userService.getAuth(),
                'studentemail': studentemail,
                'fullname': $scope.studentid
            }
        }).then(function (response) {
            //console.log("Worked!");
            //console.log(response.data);
            if (response.data == false) {
                $location.path('/home');
            } else {
                initial = true;
                getGithubIntervals();
            }
        }, function (response) {
            //fail case
            console.log("didn't work");
            $location.path('/home');
        });

        function getTaigaCourseWeightFreq() {
            $http({
                url: './taiga/course_weightFreq',
                method: "GET",
                headers: {'course': course,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                $scope.courseArrayTG = response.data;
                getGitHubCourseWeightFreq();
            }, function (response) {
                //fail case
                console.log("Didn't Work");
                getGitHubCourseWeightFreq();
            });
        }

        function getGitHubCourseWeightFreq() {
            $http({
                url: './github/course_weightFreq',
                method: "GET",
                headers: {'course': course,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                $scope.courseArrayGH = response.data;
                getSlackCourseWeightFreq();
            }, function (response) {
                //fail case
                console.log("Didn't Work");
                getSlackCourseWeightFreq();
            });
        }

        function getSlackCourseWeightFreq() {
            $http({
                url: './slack/course_weightFreq',
                method: "GET",
                headers: {'course': course,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                $scope.courseArraySK = response.data;
                getTaigaTeamWeightFreq();
            }, function (response) {
                //fail case
                console.log("Didn't Work");
                getTaigaTeamWeightFreq();
            });
        }

        function getTaigaTeamWeightFreq() {
            $http({
                url: './taiga/team_weightFreq',
                method: "GET",
                headers: {'course': course, 'team': team,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                $scope.teamArrayTG = response.data;
                getGitHubTeamWeightFreq();
            }, function (response) {
                //fail case
                console.log("Didn't Work");
                getGitHubTeamWeightFreq();
            });
        }

        function getGitHubTeamWeightFreq() {
            $http({
                url: './github/team_weightFreq',
                method: "GET",
                headers: {'course': course, 'team': team,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                $scope.teamArrayGH = response.data;
                getSlackTeamWeightFreq();
            }, function (response) {
                //fail case
                console.log("Didn't Work");
                getSlackTeamWeightFreq();
            });
        }

        function getSlackTeamWeightFreq() {
            $http({
                url: './slack/team_weightFreq',
                method: "GET",
                headers: {'course': course, 'team': team,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                $scope.teamArraySK = response.data;
                getTaigaStudentWeightFreq();
            }, function (response) {
                //fail case
                console.log("Didn't Work");
                getTaigaStudentWeightFreq();
            });
        }

        function getTaigaStudentWeightFreq() {
            $http({
                url: './taiga/student_weightFreq',
                method: "GET",
                headers: {'course': course, 'team': team, 'email': studentemail,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                $scope.studentArrayTG = response.data;
                getGitHubStudentWeightFreq();
            }, function (response) {
                //fail case
                console.log("Didn't Work");
                getGitHubStudentWeightFreq();
            });
        }

        function getGitHubStudentWeightFreq() {
            $http({
                url: './github/student_weightFreq',
                method: "GET",
                headers: {'course': course, 'team': team, 'email': studentemail,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                $scope.studentArrayGH = response.data;
                getSlackStudentWeightFreq();
            }, function (response) {
                //fail case
                console.log("Didn't Work");
                getSlackStudentWeightFreq();
            });
        }

        function getSlackStudentWeightFreq() {
            $http({
                url: './slack/student_weightFreq',
                method: "GET",
                headers: {'course': course, 'team': team, 'email': studentemail,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                //console.log("Slack");
                //console.log(response.data);
                $scope.studentArraySK = response.data;
                plotCurrentWeek();
            }, function (response) {
                //fail case
                console.log("Didn't Work");
                plotCurrentWeek();
            });
        }

        function plotCurrentWeek() {

            if($scope.courseArrayTG[0] == null){
                $scope.courseWeightTG0 = 0;
                $scope.courseFrequencyTG0 = 0;
            }else{
                $scope.courseWeightTG0 = $scope.courseArrayTG[0].weight;
                $scope.courseFrequencyTG0 = $scope.courseArrayTG[0].frequency;
            }
            if($scope.courseArrayGH[0] == null){
                $scope.courseWeightGH0 = 0;
                $scope.courseFrequencyGH0 = 0;
            }else{
                $scope.courseWeightGH0 = $scope.courseArrayGH[0].weight;
                $scope.courseFrequencyGH0 = $scope.courseArrayGH[0].frequency;
            }
            if($scope.courseArraySK[0] == null){
                $scope.courseWeightSK0 = 0;
                $scope.courseFrequencySK0 = 0;
            }else{
                $scope.courseWeightSK0 = $scope.courseArraySK[0].weight;
                $scope.courseFrequencySK0 = $scope.courseArraySK[0].frequency;
            }
            if($scope.teamArrayTG[0] == null){
                $scope.teamWeightTG0 = 0;
                $scope.teamFrequencyTG0 = 0;
            }else{
                $scope.teamWeightTG0 = $scope.teamArrayTG[0].weight;
                $scope.teamFrequencyTG0 = $scope.teamArrayTG[0].frequency;
            }
            if($scope.teamArrayGH[0] == null){
                $scope.teamWeightGH0 = 0;
                $scope.teamFrequencyGH0 = 0;
            }else{
                $scope.teamWeightGH0 = $scope.teamArrayGH[0].weight;
                $scope.teamFrequencyGH0 = $scope.teamArrayGH[0].frequency;
            }
            if($scope.teamArraySK[0] == null){
                $scope.teamWeightSK0 = 0;
                $scope.teamFrequencySK0 = 0;
            }else{
                $scope.teamWeightSK0 = $scope.teamArraySK[0].weight;
                $scope.teamFrequencySK0 = $scope.teamArraySK[0].frequency;
            }
            if($scope.studentArrayTG[0] == null){
                $scope.studentWeightTG0 = 0;
                $scope.studentFrequencyTG0 = 0;
            }else{
                $scope.studentWeightTG0 = $scope.studentArrayTG[0].weight;
                $scope.studentFrequencyTG0 = $scope.studentArrayTG[0].frequency;
            }
            if($scope.studentArrayGH[0] == null){
                $scope.studentWeightGH0 = 0;
                $scope.studentFrequencyGH0 = 0;
            }else{
                $scope.studentWeightGH0 = $scope.studentArrayGH[0].weight;
                $scope.studentFrequencyGH0 = $scope.studentArrayGH[0].frequency;
            }
            if($scope.studentArraySK[0] == null){
                $scope.studentWeightSK0 = 0;
                $scope.studentFrequencySK0 = 0;
            }else{
                $scope.studentWeightSK0 = $scope.studentArraySK[0].weight;
                $scope.studentFrequencySK0 = $scope.studentArraySK[0].frequency;
            }
            $scope.selectedWeekLabels = ['Taiga Impact', 'Taiga Freq', 'GH Impact', 'GH Freq', 'Slack Impact', 'Slack Freq'];
            $scope.selectedWeekSeries1 = "Course";
            $scope.selectedWeekSeries2 = "Team";
            $scope.selectedWeekSeries3 = "Student";
            $scope.selectedWeekData = [
                [$scope.courseWeightTG0, $scope.courseFrequencyTG0, $scope.courseWeightGH0, $scope.courseFrequencyGH0, $scope.courseWeightSK0, $scope.courseFrequencySK0],
                [$scope.teamWeightTG0, $scope.teamFrequencyTG0, $scope.teamWeightGH0, $scope.teamFrequencyGH0, $scope.teamWeightSK0, $scope.teamFrequencySK0],
                [$scope.studentWeightTG0, $scope.studentFrequencyTG0, $scope.studentWeightGH0, $scope.studentFrequencyGH0, $scope.studentWeightSK0, $scope.studentFrequencySK0]
            ];

            if(initial){
                //console.log("Initial Run for Selected Chart");
                var selectedOverallCtx = $("#radarSelected").get(0).getContext("2d");
                $scope.selectedOverallChart = new Chart(selectedOverallCtx, {
                    type: 'radar',
                    data: {
                        labels: $scope.selectedWeekLabels,
                        datasets: [{
                            fill: 'origin',
                            backgroundColor:'rgb(52, 176, 114, .25)',
                            borderColor: 'rgb(52, 176, 114)',
                            borderWidth: 2,
                            label: $scope.selectedWeekSeries3,
                            data: $scope.selectedWeekData[2],
                            pointRadius: 3,
                            pointBorderWidth: 2,
                            pointBackgroundColor: 'rgb(52, 176, 114, .25)',
                            pointBorderColor: 'rgb(68, 202, 135)',
                            pointHoverRadius: 6
                        },
                            {
                                fill: 'origin',
                                backgroundColor:'rgb(42, 137, 232, .25)',
                                borderColor: 'rgb(42, 137, 232)',
                                borderWidth: 2,
                                label: $scope.selectedWeekSeries2,
                                data: $scope.selectedWeekData[1],
                                pointRadius: 3,
                                pointBorderWidth: 2,
                                pointBackgroundColor: 'rgb(42, 137, 232, .25)',
                                pointBorderColor: 'rgb(42, 137, 232)',
                                pointHoverRadius: 6
                            },
                            {
                                fill: 'origin',
                                backgroundColor:'rgb(228, 98, 98, .25)',
                                borderColor: 'rgb(228, 98, 98)',
                                borderWidth: 2,
                                label: $scope.selectedWeekSeries1,
                                data: $scope.selectedWeekData[0],
                                pointRadius: 3,
                                pointBorderWidth: 2,
                                pointBackgroundColor: 'rgb(228, 98, 98, .25)',
                                pointBorderColor: 'rgb(228, 98, 98)',
                                pointHoverRadius: 6
                            }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        legend: {
                            display: true
                        },
                        scale: {
                            ticks: {
                                beginAtZero: true,
                                min: 0,
                                max: 3,
                                stepSize: .5
                            },
                            pointLabels: {
                                fontSize: 14
                            }
                        }
                    }
                });
                initial = false;
                plotPreviousWeek();
            } else {
                //console.log("Not Initial Run for Selected Chart");
                $('#radarSelected').remove();
                $('#selectedOverall').append('<canvas id="radarSelected" class="chart" style="width:100%; height:100%"></canvas>');
                var selectedOverallCtx = $("#radarSelected").get(0).getContext("2d");
                $scope.selectedOverallChart = new Chart(selectedOverallCtx, {
                    type: 'radar',
                    data: {
                        labels: $scope.selectedWeekLabels,
                        datasets: [{
                            fill: 'origin',
                            backgroundColor:'rgb(52, 176, 114, .25)',
                            borderColor: 'rgb(52, 176, 114)',
                            borderWidth: 2,
                            label: $scope.selectedWeekSeries3,
                            data: $scope.selectedWeekData[2],
                            pointRadius: 3,
                            pointBorderWidth: 2,
                            pointBackgroundColor: 'rgb(52, 176, 114, .25)',
                            pointBorderColor: 'rgb(52, 176, 114)',
                            pointHoverRadius: 6
                        },
                            {
                                fill: 'origin',
                                backgroundColor:'rgb(42, 137, 232, .25)',
                                borderColor: 'rgb(42, 137, 232)',
                                borderWidth: 2,
                                label: $scope.selectedWeekSeries2,
                                data: $scope.selectedWeekData[1],
                                pointRadius: 3,
                                pointBorderWidth: 2,
                                pointBackgroundColor: 'rgb(42, 137, 232, .25)',
                                pointBorderColor: 'rgb(42, 137, 232)',
                                pointHoverRadius: 6
                            },
                            {
                                fill: 'origin',
                                backgroundColor:'rgb(228, 98, 98, .25)',
                                borderColor: 'rgb(228, 98, 98)',
                                borderWidth: 2,
                                label: $scope.selectedWeekSeries1,
                                data: $scope.selectedWeekData[0],
                                pointRadius: 3,
                                pointBorderWidth: 2,
                                pointBackgroundColor: 'rgb(228, 98, 98, .25)',
                                pointBorderColor: 'rgb(228, 98, 98)',
                                pointHoverRadius: 6
                            }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        legend: {
                            display: true
                        },
                        scale: {
                            ticks: {
                                beginAtZero: true,
                                min: 0,
                                max: 3,
                                stepSize: .5
                            },
                            pointLabels: {
                                fontSize: 14
                            }
                        }
                    }
                });
            }
        }

        function plotPreviousWeek() {

            if($scope.courseArrayTG[1] == null){
                $scope.courseWeightTG1 = 0;
                $scope.courseFrequencyTG1 = 0;
            }else{
                $scope.courseWeightTG1 = $scope.courseArrayTG[1].weight;
                $scope.courseFrequencyTG1 = $scope.courseArrayTG[1].frequency;
            }
            if($scope.courseArrayGH[1] == null){
                $scope.courseWeightGH1 = 0;
                $scope.courseFrequencyGH1 = 0;
            }else{
                $scope.courseWeightGH1 = $scope.courseArrayGH[1].weight;
                $scope.courseFrequencyGH1 = $scope.courseArrayGH[1].frequency;
            }
            if($scope.courseArraySK[1] == null){
                $scope.courseWeightSK1 = 0;
                $scope.courseFrequencySK1 = 0;
            }else{
                $scope.courseWeightSK1 = $scope.courseArraySK[1].weight;
                $scope.courseFrequencySK1 = $scope.courseArraySK[1].frequency;
            }
            if($scope.teamArrayTG[1] == null){
                $scope.teamWeightTG1 = 0;
                $scope.teamFrequencyTG1 = 0;
            }else{
                $scope.teamWeightTG1 = $scope.teamArrayTG[1].weight;
                $scope.teamFrequencyTG1 = $scope.teamArrayTG[1].frequency;
            }
            if($scope.teamArrayGH[1] == null){
                $scope.teamWeightGH1 = 0;
                $scope.teamFrequencyGH1 = 0;
            }else{
                $scope.teamWeightGH1 = $scope.teamArrayGH[1].weight;
                $scope.teamFrequencyGH1 = $scope.teamArrayGH[1].frequency;
            }
            if($scope.teamArraySK[1] == null){
                $scope.teamWeightSK1 = 0;
                $scope.teamFrequencySK1 = 0;
            }else{
                $scope.teamWeightSK1 = $scope.teamArraySK[1].weight;
                $scope.teamFrequencySK1 = $scope.teamArraySK[1].frequency;
            }
            if($scope.studentArrayTG[1] == null){
                $scope.studentWeightTG1 = 0;
                $scope.studentFrequencyTG1 = 0;
            }else{
                $scope.studentWeightTG1 = $scope.studentArrayTG[1].weight;
                $scope.studentFrequencyTG1 = $scope.studentArrayTG[1].frequency;
            }
            if($scope.studentArrayGH[1] == null){
                $scope.studentWeightGH1 = 0;
                $scope.studentFrequencyGH1 = 0;
            }else{
                $scope.studentWeightGH1 = $scope.studentArrayGH[1].weight;
                $scope.studentFrequencyGH1 = $scope.studentArrayGH[1].frequency;
            }
            if($scope.studentArraySK[1] == null){
                $scope.studentWeightSK1 = 0;
                $scope.studentFrequencySK1 = 0;
            }else{
                $scope.studentWeightSK1 = $scope.studentArraySK[1].weight;
                $scope.studentFrequencySK1 = $scope.studentArraySK[1].frequency;
            }

            $scope.overallWeekLabels = ['Taiga Impact', 'Taiga Freq', 'GH Impact', 'GH Freq', 'Slack Impact', 'Slack Freq'];
            $scope.overallWeekSeries1 = "Course";
            $scope.overallWeekSeries2 = "Team";
            $scope.overallWeekSeries3 = "Student";
            $scope.overallWeekData = [
                [$scope.courseWeightTG1, $scope.courseFrequencyTG1, $scope.courseWeightGH1, $scope.courseFrequencyGH1, $scope.courseWeightSK1, $scope.courseFrequencySK1],
                [$scope.teamWeightTG1, $scope.teamFrequencyTG1, $scope.teamWeightGH1, $scope.teamFrequencyGH1, $scope.teamWeightSK1, $scope.teamFrequencySK1],
                [$scope.studentWeightTG1, $scope.studentFrequencyTG1, $scope.studentWeightGH1, $scope.studentFrequencyGH1, $scope.studentWeightSK1, $scope.studentFrequencySK1]
            ];

            var totalOverallCtx = $("#radarOverall").get(0).getContext("2d");
            $scope.totalOverallChart = new Chart(totalOverallCtx, {
                type: 'radar',
                data: {
                    labels: $scope.overallWeekLabels,
                    datasets: [{
                        fill: 'origin',
                        backgroundColor:'rgb(52, 176, 114, .25)',
                        borderColor: 'rgb(52, 176, 114)',
                        borderWidth: 2,
                        label: $scope.overallWeekSeries3,
                        data: $scope.overallWeekData[2],
                        pointRadius: 3,
                        pointBorderWidth: 2,
                        pointBackgroundColor: 'rgb(52, 176, 114, .25)',
                        pointBorderColor: 'rgb(52, 176, 114)',
                        pointHoverRadius: 6
                    },
                        {
                            fill: 'origin',
                            backgroundColor:'rgb(42, 137, 232, .25)',
                            borderColor: 'rgb(42, 137, 232)',
                            borderWidth: 2,
                            label: $scope.overallWeekSeries2,
                            data: $scope.overallWeekData[1],
                            pointRadius: 3,
                            pointBorderWidth: 2,
                            pointBackgroundColor: 'rgb(42, 137, 232, .25)',
                            pointBorderColor: 'rgb(42, 137, 232)',
                            pointHoverRadius: 6
                        },
                        {
                        fill: 'origin',
                        backgroundColor:'rgb(228, 98, 98, .25)',
                        borderColor: 'rgb(228, 98, 98)',
                        borderWidth: 2,
                        label: $scope.overallWeekSeries1,
                        data: $scope.overallWeekData[0],
                        pointRadius: 3,
                        pointBorderWidth: 2,
                        pointBackgroundColor: 'rgb(228, 98, 98, .25)',
                        pointBorderColor: 'rgb(228, 98, 98)',
                        pointHoverRadius: 6
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    legend: {
                        display: true
                    },
                    scale: {
                        ticks: {
                            beginAtZero: true,
                            min: 0,
                            max: 3,
                            stepSize: .5
                        },
                        pointLabels: {
                            fontSize: 14
                        }
                    }
                }
            });
        }
        function getTaigaActivity() {
            $scope.taigaMaxY = 0;
            $http({
                url: './taiga/student_activity',
                method: "GET",
                headers: {'course': course, 'team': team, 'email': studentemail}
            }).then(function (response) {
                //console.log("Worked!");
                //console.log(response.data);
                $scope.studentActivity = response.data;
                $scope.dataForTaigaStudentActivity =  getDataForStudentTaigaActivityCharts(response.data);
            }, function (response) {
                //fail case
                console.log("Didn't Work");

            });
        }
        $scope.optionsForTaigaStudentActivity = {

            chart: {
                type: 'lineChart',
                height: 450,
                margin : {
                    top: 50,
                    right: 150,
                    bottom: 100,
                    left:100
                },

                x: function(d){ return d[0]; },
                y: function(d){ return d[1]; },

                useInteractiveGuideline: true,

                xAxis: {
                    axisLabel: 'Date of Activity',
                    tickFormat: function(d) {
                        return d3.time.format('%Y-%m-%d')(new Date(d))
                    },

                    showMaxMin: false,
                    staggerLabels: false
                },

                yAxis: {
                    axisLabel: 'Taiga Task Activity',
                    axisLabelDistance: 0
                    },

                yDomain:[0, $scope.taigaMaxY]

            }
        };

        function getDataForStudentTaigaActivityCharts(array){

            var data = []; var inProgress = []; var toTest = []; var done = [];var expected = []; var maxArr = [];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];var valueset2 = [];var valueset3 = [];var valueset4 = [];

                valueset1.push(array[i].rawWeekEnding*1000);
                valueset1.push(array[i].inProgressActivity);

                valueset2.push(array[i].rawWeekEnding*1000);
                valueset2.push(array[i].toTestActivity);

                valueset3.push(array[i].rawWeekEnding*1000);
                valueset3.push(array[i].doneActivity);

                valueset4.push(array[i].rawWeekEnding*1000);
                valueset4.push(3);

                inProgress.push(valueset1);
                toTest.push(valueset2);
                done.push(valueset3);
                expected.push(valueset4);
				maxArr.push(array[i].inProgressActivity);
                maxArr.push(array[i].toTestActivity);
                maxArr.push(array[i].doneActivity);
			}
			var taigaLineChartMax = Math.ceil(Math.max.apply(Math, maxArr));
            $scope.taigaMaxY = getTaigaLineChartMax(taigaLineChartMax);

            data.push({color: "#6799ee", key: "IN PROGRESS", values: inProgress, strokeWidth: 2});
            data.push({color: "#000000", key: "READY FOR TEST", values: toTest, strokeWidth: 2});
            data.push({color: "#2E8B57", key: "CLOSED", values: done, strokeWidth: 2});
            data.push({color: "#8f65b6", key: "EXPECTED", values: expected, classed: "dashed"});

            return data;
        }
		function getTaigaLineChartMax(taigaLineChartMax){
            if(taigaLineChartMax > 50){
                return 50;
            } else {
                return taigaLineChartMax;
            }
        }
        function getGithubIntervals() {
            $http({
                url: './github/student_intervals',
                method: "GET",
                headers: {'course': course, 'team': team, 'email': studentemail}
            }).then(function (response) {
                //console.log("Worked This shows the intervals!");
                //console.log(response.data);
                $scope.studentIntervals = response.data;

                if ($rootScope.Period.start == null && $rootScope.Period.end == null) {
                    $rootScope.Period.start = new Date($scope.courseIntervals[$scope.courseIntervals.length - 1].weekBeginning);
                    $rootScope.Period.end = new Date();
                    if($rootScope.Period.start.valueOf()<Date.parse($scope.startDate))
                        $rootScope.Period.start = new Date(Date.parse($scope.startDate));
                    console.log("initial end: "+$rootScope.Period.end.value);
                    if($rootScope.Period.end.valueOf()>Date.parse($scope.endDate))
                        $rootScope.Period.end = new Date(Date.parse($scope.endDate));
                    $scope.IntervalChangedEnd();
                }
                else {
                    $scope.IntervalChangedEnd();
                }
                
                /*if($rootScope.rawWeekBeginning == null && $rootScope.rawWeekEnding == null) {
                    $scope.SelectedWeekBeginning = $scope.studentIntervals[$scope.studentIntervals.length-1];
                    $rootScope.rawWeekBeginning = $scope.SelectedWeekBeginning.weekBeginning;
                    $scope.SelectedWeekEnding =  $scope.studentIntervals[$scope.studentIntervals.length-1];
                    $scope.IntervalChangedEnd($scope.SelectedWeekEnding.weekEnding);
                }
                else {
                    $scope.SelectedWeekBeginning.weekBeginning = $rootScope.rawWeekBeginning/!*$scope.studentIntervals.find(function(element) {
                        return element.rawWeekBeginning == $rootScope.rawWeekBeginning;
                    });*!/
                    $scope.SelectedWeekEnding.weekEnding = $rootScope.rawWeekEnding/!*$scope.studentIntervals.find(function(element) {
                        return element.rawWeekEnding == $rootScope.rawWeekEnding;
                    });*!/
                    $scope.IntervalChangedEnd($rootScope.rawWeekEnding);
                }*/
                $('select option')
                    .filter(function() {
                        return !this.value || $.trim(this.value).length == 0 || $.trim(this.text).length == 0;
                    })
                    .remove();
            }, function (response) {
                //fail case
                $('select option')
                    .filter(function() {
                        return !this.value || $.trim(this.value).length == 0 || $.trim(this.text).length == 0;
                    })
                    .remove();
                console.log("Didn't Work");
            });
        }

        $scope.IntervalChangedBegin = function () {
            //console.log("WeekBeginning: " + $rootScope.rawWeekBeginning);
            if ($rootScope.Period.end != null) {
                if($rootScope.Period.end<$rootScope.Period.start){
                    $rootScope.Period.end = $rootScope.Period.start;
                    $rootScope.Period.end.setHours(23,59,59,999);
                }
                $rootScope.Period.start.setHours(0,0,0,0);
                getAllStudentTabsData();
            }
        };
        $scope.IntervalChangedEnd = function () {
            //console.log("WeekEnding: " + $rootScope.rawWeekEnding);
            if ($rootScope.Period.start != null) {
                if($rootScope.Period.start>$rootScope.Period.end){
                    $rootScope.Period.start = $rootScope.Period.end;
                    $rootScope.Period.start.setHours(0,0,0,0);
                }
                $rootScope.Period.end.setHours(23,59,59,999);
                getAllStudentTabsData();
            }
        };
        function getAllStudentTabsData(){
            getTaigaCourseWeightFreq();
            $http({
                url: './taiga/student_tasks',
                method: "GET",
                headers: {
                    'course': course,
                    'team': team,
                    'email': studentemail,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
               // console.log("Worked! Begin Changed: ");
                //console.log(response.data);

                $scope.studentTasks = response.data;
                $scope.dataForTaigaStudentTasks = getDataForTaigaStudentTasks(response.data);
            });
            getTaigaActivity();
            $http({
                url: './github/commits_student',
                method: "GET",
                headers: {
                    'course': course,
                    'team': team,
                    'email': studentemail,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                //console.log("Worked This is what the GitHub Data is showing: !");
                processGitHubCommitMax(response.data);
            }, function (response) {
                //fail case
                console.log("didn't work");
            });
            $http({
                url: './slack/student_messages',
                method: "GET",
                headers: {'course': course,
                    'team': team,
                    'email': studentemail,
                    'weekBeginning': new Date($rootScope.Period.start).valueOf(),
                    'weekending': new Date($rootScope.Period.end).valueOf()
                    /*'weekBeginning': new Date($rootScope.rawWeekBeginning).valueOf(),
                    'weekending': new Date($rootScope.rawWeekEnding).valueOf()*/
                }
            }).then(function (response) {
                //console.log("SlackStudentMessages");
                //console.log(response.data);
                $scope.studentMessages = response.data;
                $scope.dataForSlackStudentMessages = getDataForSlackStudentMessages(response.data);
            });
            getSlackActivity();


        }

        $scope.optionsForTaigaStudentTasks = {

            chart: {
                type: 'multiBarChart',
                height: 450,
                margin : {
                    top: 50,
                    right: 150,
                    bottom: 100,
                    left:100
                },

                x: function(d){ return d[0]; },
                y: function(d){ return d[1]; },

                clipEdge: true,
                duration: 500,
                stacked: false,

                xAxis: {
                    axisLabel: 'Date of Activity',
                    showMaxMin: false
                },

                yAxis: {
                    axisLabel: 'Taiga Task Totals',
                    axisLabelDistance: 0
                }
            }
        };

        function getDataForTaigaStudentTasks(array){

            var data = []; var inProgress = []; var toTest = []; var done =[];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];var valueset2 = [];var valueset3 = [];

                valueset1.push(array[i].date);
                valueset1.push(array[i].inProgress);

                valueset2.push(array[i].date);
                valueset2.push(array[i].toTest);

                valueset3.push(array[i].date);
                valueset3.push(array[i].done);

                inProgress.push(valueset1);
                toTest.push(valueset2);
                done.push(valueset3);
            }

            data.push({color: "#6799ee", key: "IN PROGRESS", values: inProgress});
            data.push({color: "#000000", key: "READY FOR TEST", values: toTest});
            data.push({color: "#2E8B57", key: "CLOSED", values: done});

            return data;
        }

       function processGitHubCommitMax(array){
            var commits = []; var linesOfCodeAdded = []; var linesOfCodeDeleted = [];
            for (var i = 0; i < array.length; i++){
                commits.push(array[i].commits);
                linesOfCodeAdded.push(array[i].linesOfCodeAdded/100);
                linesOfCodeDeleted.push(array[i].linesOfCodeDeleted/100);
            }

            var maxArray = [Math.max.apply(Math, commits), Math.max.apply(Math, linesOfCodeAdded), Math.max.apply(Math, linesOfCodeDeleted)];
            var gitHubBarChartMax = Math.ceil(Math.max.apply(Math, maxArray));
            $scope.commitMaxY = getGitHubBarChartMax(gitHubBarChartMax);
            $scope.dataForGitHubStudentCommits =  getDataForGitHubStudentCommitsCharts(array);
            commitOptions();
            getGitHubWeightData();

           $scope.dataForGitHubTotalsDaily = getDataForGitHubTotalsDaily(array);
        }

        function getDataForGitHubTotalsDaily(array){

            var student_name;
            var daysOfWeek = [
                "Sun","Mon","Tues","Wed","Thur","Fri","Sat"
            ];


            if(array[0].gitHubPK.username!=null) {
                student_name = array[0].gitHubPK.username;
            }
            var dataDaily = [];
            if(student_name != null)
            {
                var commits = [];
                var add = [];
                var del = [];
                var total = [];
                var day = [];

                for (var i = 0; i < array.length; i++){
                    commits.push(array[i].commits);
                    add.push(array[i].linesOfCodeAdded);
                    del.push(array[i].linesOfCodeDeleted);
                    total.push(array[i].linesOfCodeAdded + array[i].linesOfCodeDeleted);

                    var currentDate = new Date(array[i].gitHubPK.date);
                    var mm = currentDate.getMonth() + 1;
                    if(mm<10) {
                        mm = "0" + (mm);
                    }
                    var dd = currentDate.getUTCDate();
                    if(dd<10) {
                        dd = "0" + (dd);
                    }

                    day.push(mm + "/" + dd + "/" + currentDate.getFullYear() + " (" + daysOfWeek[currentDate.getDay()] + ") ");
                }

                dataDaily.push({student: student_name, commits: commits, add: add, del: del,total: total, day: day});
                return dataDaily;
            }

            var commits = []; var linesOfCodeAdded = []; var linesOfCodeDeleted = [];
            var data = [];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];var valueset2 = [];var valueset3 = [];

                valueset1.push(array[i].gitHubPK.date);
                valueset1.push(array[i].commits);

                valueset2.push(array[i].gitHubPK.date);
                valueset2.push(array[i].linesOfCodeAdded/100);

                valueset3.push(array[i].gitHubPK.date);
                valueset3.push(array[i].linesOfCodeDeleted/100);

                commits.push(valueset1);
                linesOfCodeAdded.push(valueset2);
                linesOfCodeDeleted.push(valueset3);
            }

            data.push({color: "#6799ee", key: "Commits", values: commits });
            data.push({color: "#000000", key: "Lines Of Code Added/100", values: linesOfCodeAdded});
            data.push({color: "#2E8B57", key: "Lines Of Code Deleted/100", values: linesOfCodeDeleted});

            return data;
        }



        function getGitHubBarChartMax(gitHubBarChartMax){

            if(gitHubBarChartMax > 50){
                return 50;
            } else {
                return gitHubBarChartMax;
            }
        }

        function commitOptions() {

            $scope.optionsForGitHubStudentCommits = {

                chart: {
                    type: 'multiBarChart',
                    height: 450,
                    margin: {
                        top: 50,
                        right: 150,
                        bottom: 100,
                        left: 100
                    },

                    legend: {
                        margin: {
                            top: 0,
                            right: 0,
                            bottom: 20,
                            left: 0
                        },
                        maxKeyLength: 100
                    },

                    x: function (d) {
                        return d[0];
                    },
                    y: function (d) {
                        return d[1];
                    },

                    clipEdge: true,
                    duration: 500,
                    stacked: false,

                    xAxis: {
                        axisLabel: 'Date of Activity',
                        showMaxMin: false
                    },

                    yAxis: {
                        axisLabel: 'GitHub Commit Counts',
                        axisLabelDistance: 0
                    },

                    yDomain: [0, $scope.commitMaxY]

                }
            };
        }

        //* Function to Parse GitHub CommitData for MultiBar Chart * //

        function getDataForGitHubStudentCommitsCharts(array){

            var commits = []; var linesOfCodeAdded = []; var linesOfCodeDeleted = []; 
            var data = [];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];var valueset2 = [];var valueset3 = [];

                valueset1.push(array[i].gitHubPK.date);
                valueset1.push(array[i].commits);

                valueset2.push(array[i].gitHubPK.date);
                valueset2.push(array[i].linesOfCodeAdded/100);

                valueset3.push(array[i].gitHubPK.date);
                valueset3.push(array[i].linesOfCodeDeleted/100);

                commits.push(valueset1);
                linesOfCodeAdded.push(valueset2);
                linesOfCodeDeleted.push(valueset3);
            }

            data.push({color: "#6799ee", key: "Commits", values: commits });
            data.push({color: "#000000", key: "Lines Of Code Added/100", values: linesOfCodeAdded});
            data.push({color: "#2E8B57", key: "Lines Of Code Deleted/100", values: linesOfCodeDeleted});

            return data;
        }

        function getGitHubWeightData() {
            $http({
                url: './github/weights_student',
                method: "GET",
                headers: {'course': course, 'team': team, 'email': studentemail}
            }).then(function (response) {
                //console.log("Worked This is what the GitHub Weight is showing: !");
                //console.log(response.data);
                $scope.dataForGitHubStudentWeight= getDataForGitHubStudentWeightCharts(response.data);
                getSlackActivity();
            }, function (response) {
                //fail case
                console.log("didn't work");
                getSlackActivity();
            });
        }

        $scope.optionsForGitHubStudentWeight = {

            chart: {
                type: 'lineChart',
                height: 450,
                margin : {
                    top: 50,
                    right: 150,
                    bottom: 100,
                    left:100
                },

                x: function(d){ return d[0]; },
                y: function(d){ return d[1]; },

                useInteractiveGuideline: true,

                xAxis: {
                    axisLabel: 'Date of Activity',
                    tickFormat: function(d) {
                        return d3.time.format('%Y-%m-%d')(new Date(d))
                    },

                    showMaxMin: false,
                    staggerLabels: false
                },

                yAxis: {
                    axisLabel: 'GitHub Task Updates Weight',
                    axisLabelDistance: 0,
                    tickValues:  [0, 3, 6, 9, 12, 15]
                },

                yDomain:[0, 15]

            }
        };


        ////* Function to Parse GitHub Weight for Line Chart * ////

        function getDataForGitHubStudentWeightCharts(array){

            var weight = []; var expected = []; var data = [];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];var valueset2 = [];

                valueset1.push(Date.parse(array[i].gitHubPK.date));
                valueset1.push(array[i].weight);

                valueset2.push(Date.parse(array[i].gitHubPK.date));
                valueset2.push(4);

                weight.push(valueset1);
                expected.push(valueset2);
            }

            data.push({color: "#6799ee", key: "Weight", values: weight, strokeWidth: 2});
            data.push({color: "#000000", key: "Expected", values: expected, strokeWidth: 2});

            return data;
        }

        function getSlackActivity() {
            $http({
                url: './slack/student_totals',
                method: "GET",
                headers: {'course': course, 'team': team, 'email': studentemail}
            }).then(function (response) {
                //console.log("SlackActivity");
                //console.log(response.data);
                $scope.slackStudentActivity = response.data;
                $scope.dataForSlackStudentActivity = getDataForStudentSlackActivityCharts(response.data);
                }, function (response) {
                //fail case
                console.log("didn't work");
            });
        }

        $scope.optionsForSlackStudentActivity = {

            chart: {
                type: 'lineChart',
                height: 450,
                margin : {
                    top: 50,
                    right: 150,
                    bottom: 100,
                    left:100
                },

                x: function(d){ return d[0]; },
                y: function(d){ return d[1]; },

                useInteractiveGuideline: true,

                xAxis: {
                    axisLabel: 'Date of Activity',
                    tickFormat: function(d) {
                        return d3.time.format('%Y-%m-%d')(new Date(d))
                    },

                    showMaxMin: false,
                    staggerLabels: false
                },

                yAxis: {
                    axisLabel: 'Slack Message Activity',
                    axisLabelDistance: 0,
                    tickValues:  [0, 25, 50, 100, 125, 125, 175, 200]
                },

                yDomain:[0, 200]

            }
        };

        function getDataForStudentSlackActivityCharts(array){

            var data = []; var total = [];var expected = [];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];var valueset2 = [];

                valueset1.push(Date.parse(array[i].date));//*1000);
                valueset1.push(array[i].total);

                valueset2.push(Date.parse(array[i].date));//*1000);
                valueset2.push(100);

                total.push(valueset1);
                expected.push(valueset2);
            }

            data.push({color: "#2E8B57", key: "TOTAL", values: total, strokeWidth: 2});
            data.push({color: "#8f65b6", key: "EXPECTED", values: expected, classed: "dashed"});

            return data;
        }

        $scope.optionsForSlackStudentMessages = {

            chart: {
                type: 'multiBarChart',
                height: 450,
                margin : {
                    top: 50,
                    right: 150,
                    bottom: 100,
                    left:100
                },

                x: function(d){ return d[0]; },
                y: function(d){ return d[1]; },

                clipEdge: true,
                duration: 500,
                stacked: false,

                xAxis: {
                    axisLabel: 'Date of Activity',
                    showMaxMin: false
                },

                yAxis: {
                    axisLabel: 'Slack Message Totals',
                    axisLabelDistance: 0
                }
            }
        };

        function getDataForSlackStudentMessages(array){

            var data = []; var total = [];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];

                valueset1.push(array[i].date);
                if(array[i]==null){
                    valueset1.push(0);
                }else{
                    valueset1.push(array[i].total);
                }
                total.push(valueset1);
            }

            data.push({color: "#2E8B57", key: "TOTAL", values: total});

            return data;
        }

        var fireRefreshEventOnWindow = function () {
            var evt = document.createEvent("HTMLEvents");
            evt.initEvent('resize', true, false);
            window.dispatchEvent(evt);
        };

    }])
    .controller("AgileToolAdmin", ['$rootScope', '$scope', '$http', '$window', function ($rootScope, $scope, $http, $window) {

        $rootScope.provisionMode = false;

        $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";

        $scope.updateGitHubCommitWeight = function () {
            $http({
                url: './github/weight',
                method: "POST"
            }).then(function (response) {
                //console.log("Worked!");
                $window.alert("GitHub Commit and Weight Data Successfully Updated");
            }, function (response) {
                //console.log("didn't work");
                $window.alert("GitHub Commit and Weight Data Not Successfully Updated");
            });

        };

        $scope.updateTaigaTaskTotals = function () {
            $http({
                url: './taiga/Update/Tasks',
                method: "POST"
            }).then(function (response) {
                $window.alert("Taiga Task Totals Data Successfully Updated");
            }, function (response) {
                //console.log("didn't work");
                $window.alert("Taiga Task Totals Data Not Successfully Updated");
            });

        };

        $scope.updateSlackUsers = function () {
            $http({
                url: './slack/update_users',
                method: "POST"
            }).then(function (response) {
                $window.alert("Slack Users Successfully Updated");
            }, function (response) {
                //console.log("didn't work");
                $window.alert("Slack Users Not Successfully Updated");
            });

        };

        $scope.updateSlackMessageTotals = function () {
            $http({
                url: './slack/update_messageTotals',
                method: "POST"
            }).then(function (response) {
                $window.alert("Slack Message Totals Successfully Updated");
            }, function (response) {
                //console.log("didn't work");
                $window.alert("Slack Message Totals Not Successfully Updated");
            });

        };

        $scope.updateTaigaProjects = function () {
            $http({
                url: './taiga/Update/Projects',
                method: "POST"
            }).then(function (response) {
                $window.alert("Taiga Projects Successfully Updated");
            }, function (response) {
                //fail case
                $window.alert("Taiga Projects Not Successfully Updated");
            });

        };

        $scope.updateTaigaMemberships = function () {
            $http({
                url: './taiga/Update/Memberships',
                method: "POST"
            }).then(function (response) {
                $window.alert("Taiga Memberships Successfully Updated");
            }, function (response) {
                //fail case
                $window.alert("Taiga Memberships Not Successfully Updated");
            });

        };
    }])
    .controller("newCourseStudents", ['$rootScope', '$scope', '$http', '$window', '$timeout', 'provisionService', 'courseCreateService', 'teamCreateService',
        function ($rootScope, $scope, $http, $window, $timeout, provisionService, courseCreateService, teamCreateService) {

            $rootScope.provisionMode = true;

            $scope.tab = 4;

            $scope.setTab = function (newTab) {
                $scope.tab = newTab;
            };

            $scope.isSet = function (tabNum) {
                return $scope.tab === tabNum;
            };

        var course = courseCreateService.getCourse();
        //console.log("GetCourse: " + course)
            $scope.currentCourse = course;

        var teamIndex = -1;

        var fromCSV = false;

        var nextIndex = -1;

        var url = "http://cassess.fulton.asu.edu/cassess/#/";
        //var url = "http://localhost:8090/#/";

        $scope.student = {};

        $scope.students = [];

        $scope.teams = [];

        $scope.message = "";

        $scope.currentTeam = "";

        $scope.teamsArray = [];

        if ($rootScope.coursePackage.course === course) {
            if($rootScope.coursePackage.teams){
                if($rootScope.coursePackage.teams.length != 0) {
                    teamIndex = 0;
                    $scope.teams = $rootScope.coursePackage.teams;
                    for (var i in $scope.teams) {
                        $scope.teamsArray.push($scope.teams[i].team_name);
                        //console.log($scope.teams[i].team_name);
                    }

                } else {
                    window.location.href = url+'create_teams';
                }
            } else {
                window.location.href = url+'create_teams';
            }
        } else {
            window.location.href = url+'create_course';
        }
        if (teamIndex == -1){
            window.location.href = url+'create_teams';
        }
        if(course === null){
            window.location.href = url+'create_course';
        }

        $scope.setTeam = function() {
            for (var i in $rootScope.coursePackage.teams) {
                if ($rootScope.coursePackage.teams[i].team_name === $scope.selectedTeam.team_name) {
                    $scope.students = $rootScope.coursePackage.teams[i].students;
                    if(!$rootScope.coursePackage.teams[i].students){
                        $rootScope.coursePackage.teams[i].students = [];
                    }
                    teamIndex = i;
                    //console.log("Team Index: " + i);
                    teamCreateService.setTeam($rootScope.coursePackage.teams[i].team_name);
                    $scope.currentTeam = $scope.selectedTeam.team_name;
                }
            }
        };

        $scope.setClickedStudent = function(index){
            $scope.selectedRow = index;
            $scope.selectedStudent = $scope.students[index];
        };

        $scope.saveStudent = function() {
            if($scope.currentTeam != ""){
                    if($scope.enteredEmail != null && $scope.enteredEmail != ''){
                        if($scope.enteredName != null && $scope.enteredName != ''){
                            if($scope.enteredPassword != null && $scope.enteredPassword != ''){
                                if($scope.enteredGitHubUsername != null && $scope.enteredGitHubUsername != ''){
                                    if($scope.enteredTaigaUsername != null && $scope.enteredTaigaUsername != '') {
                                        $scope.student.email = $scope.enteredEmail;
                                        $scope.student.full_name = $scope.enteredName;
                                        $scope.student.password = $scope.enteredPassword;
                                        $scope.student.github_username = $scope.enteredGitHubUsername;
                                        $scope.student.taiga_username = $scope.enteredTaigaUsername;
                                        $scope.student.slack_username = $scope.enteredSlackUsername;
                                        var exists = false;
                                        for (var i in $scope.students) {
                                            if ($scope.students[i].email === $scope.student.email) {
                                                $scope.students[i] = $scope.student;
                                                //console.log("Students exists: " + $scope.student.email + "; replacing");
                                                exists = true;
                                            }
                                        }
                                        if (!exists) {
                                            if (!$scope.students || $scope.students.length === 0) {
                                                //console.log("Students 1st entry: " + $scope.student.email);
                                                $scope.students = [$scope.student];
                                            } else {
                                                $scope.students.push($scope.student);
                                                //console.log("Pushing student: " + $scope.student.email);
                                            }
                                        }
                                        $scope.clearStudentForm();
                                        $scope.student = {};
                                        //console.log("Next Index: " + nextIndex);
                                        //console.log("Current Index: " + teamIndex);
                                        //console.log("From CSV: " + fromCSV);
                                        if (!fromCSV || nextIndex != teamIndex) {
                                            $scope.applyChanges();
                                        }
                                    } else {
                                            $scope.message = 'Please Enter Taiga username prior to saving a student';
                                        }
                                    } else {
                                        $scope.message = 'Please Enter GitHub username prior to saving a student';
                                    }
                            } else {
                                $scope.message = 'Please Enter Password prior to saving a student';
                            }
                        } else {
                            $scope.message = 'Please Enter Name prior to saving a student';
                        }
                    } else {
                        $scope.message = 'Please Enter Email prior to saving a student';
                    }
                } else {
                    $scope.message = 'Please Select a team prior to saving a student';
                }
        };

        $scope.saveStudentsJSON = function(studentsArray) {
            fromCSV = true;
            if(studentsArray[0]) {
                if (studentsArray[0].email) {
                    if (studentsArray[0].email != null && studentsArray[0].email != '') {
                        var lastIndex = -1;
                        var j = 0;
                        for (var i in studentsArray) {
                            var arrayIndex = $scope.teamsArray.indexOf(studentsArray[i].team_name);
                            ++j;
                            //console.log("i value: " + i);
                            //console.log("next i value: " + j);
                            if(studentsArray[j]){
                                    if(studentsArray[j].team_name){
                                    nextIndex = $scope.teamsArray.indexOf(studentsArray[j].team_name);
                                    //console.log("NextTeam: " + studentsArray[j].team_name + " at index: " + nextIndex);
                                } else {
                                    nextIndex = -1;
                                }
                            } else {
                                nextIndex = -1;
                            }
                            if(arrayIndex >= 0){
                                teamIndex = arrayIndex;
                                if(arrayIndex != lastIndex){
                                    $scope.students = $rootScope.coursePackage.teams[teamIndex].students;
                                }
                                //console.log("Index " + arrayIndex + "; Team " + $scope.teams[teamIndex].team_name + " for Student: " + studentsArray[i].email);
                                $scope.selectedTeam = $scope.teams[teamIndex];
                                $scope.enteredEmail = studentsArray[i].email;
                                $scope.enteredName = studentsArray[i].full_name;
                                $scope.enteredPassword = studentsArray[i].password;
                                $scope.enteredGitHubUsername = studentsArray[i].github_username;
                                $scope.enteredTaigaUsername = studentsArray[i].taiga_username;
                                $scope.enteredSlackUsername = studentsArray[i].slack_username;
                                $scope.currentTeam = $scope.selectedTeam.team_name;
                                $scope.saveStudent();
                                $scope.$apply();
                                lastIndex = arrayIndex;
                            } else {
                                alert(studentsArray[i].team_name + ' does not exist');
                            }
                        }
                    } else {
                        $scope.message = 'No Student data in uploaded csv';
                    }
                } else {
                    $scope.message = 'No Student data in uploaded csv';
                }
            } else {
                $scope.message = 'No Student data in uploaded csv';
            }
            fromCSV = false;
        };

        $scope.removeStudent = function() {
            for (var i in $scope.students) {
                var email = $scope.selectedStudent.email;
                if ($scope.students[i].email === email) {
                    $scope.students.splice(i, 1);
                    $scope.selectedRow = -1;
                }
            }
        };

        $scope.clearStudentForm = function() {
                $scope.enteredName = '';
                $scope.enteredEmail = '';
                $scope.enteredPassword = '';
                $scope.enteredGitHubUsername = '';
                $scope.enteredTaigaUsername = '';
                $scope.enteredSlackUsername = '';
        };

        $scope.editStudent = function() {
            var email = $scope.selectedStudent.email;
            for (var i in $scope.students) {
                if ($scope.students[i].email === email) {
                    $scope.student = angular.copy($scope.students[i]);
                }
            }
            $scope.enteredName = $scope.student.full_name;
            $scope.enteredEmail = $scope.student.email;
            $scope.enteredPassword = $scope.student.password;
            $scope.enteredGitHubUsername = $scope.student.github_username;
            $scope.enteredTaigaUsername = $scope.student.taiga_username;
            $scope.enteredSlackUsername = $scope.student.slack_username;
        };

        $scope.applyChanges = function() {
            if ($scope.selectedTeam.team_name) {
                if($scope.selectedTeam.team_name != null || $scope.selectedTeam.team_name != ''){
                    //console.log("Applying Changes for team " + $scope.selectedTeam.team_name + " at index " + teamIndex);
                    $rootScope.coursePackage.teams[teamIndex].students = $scope.students;
                    $scope.message = "Successfully saved to " + $rootScope.coursePackage.teams[teamIndex].team_name;
                } else {
                    $scope.message = "Please first select a Team to save Student to"
                }
            } else {
                $scope.message = "Please first select a Team to save Student to"
            }

        };

        // CSV Reading Functions---------------------------------
        $scope.handleFiles = function(files) {
            if (window.FileReader) {
                $scope.getAsText(files[0]);
            } else {
                alert('FileReader is not supported in this browser.');
            }
        };

        $scope.getAsText = function(fileToRead) {
            var reader = new FileReader();
            reader.onload = $scope.loadHandler;
            reader.onerror = $scope.errorHandler;
            reader.readAsText(fileToRead);
        };

        $scope.loadHandler = function(event) {
            var csv = event.target.result;
            $scope.processData(csv);
        };

        $scope.processData = function(csv) {
            var csvArray = $scope.CSV2JSON(csv);
            //console.log(csvArray);
            //console.log(JSON.parse(csvArray));
            $scope.saveStudentsJSON(JSON.parse(csvArray));
        };

        $scope.errorHandler = function(evt) {
            if(evt.target.error.name == "NotReadableError") {
                alert("Cannot read file !");
            }
        };

        function CSVToArray(strData, strDelimiter) {
            strDelimiter = (strDelimiter || ",");
            var objPattern = new RegExp((
            "(\\" + strDelimiter + "|\\r?\\n|\\r|^)" +
            "(?:\"([^\"]*(?:\"\"[^\"]*)*)\"|" +
            "([^\"\\" + strDelimiter + "\\r\\n]*))"), "gi");
            var arrData = [[]];
            var arrMatches = null;
            while (arrMatches = objPattern.exec(strData)) {
                var strMatchedDelimiter = arrMatches[1];
                if (strMatchedDelimiter.length && (strMatchedDelimiter != strDelimiter)) {
                    arrData.push([]);
                }
                if (arrMatches[2]) {
                    var strMatchedValue = arrMatches[2].replace(
                        new RegExp("\"\"", "g"), "\"");
                } else {
                    var strMatchedValue = arrMatches[3];
                }
                arrData[arrData.length - 1].push(strMatchedValue);
            }
            return (arrData);
        }
        $scope.CSV2JSON = function(csv) {
            var array = CSVToArray(csv);
            var objArray = [];
            for (var i = 1; i < array.length; i++) {
                objArray[i - 1] = {};
                for (var k = 0; k < array[0].length && k < array[i].length; k++) {
                    var key = array[0][k];
                    objArray[i - 1][key] = array[i][k]
                }
            }
            var json = JSON.stringify(objArray);
            var str = json.replace(/},/g, "},\r\n");
            return str;
        }

    }])
    .controller("newCourseAdmins", ['$rootScope', '$scope', '$http', '$window', '$timeout', 'provisionService', 'courseCreateService',
        function ($rootScope, $scope, $http, $window, $timeout, provisionService, courseCreateService) {

            $rootScope.provisionMode = true;

            $scope.tab = 2;

            $scope.setTab = function (newTab) {
                $scope.tab = newTab;
            };

            $scope.isSet = function (tabNum) {
                return $scope.tab === tabNum;
            };

            $rootScope.provisionMode = true;

            var course = courseCreateService.getCourse();

            var url = "http://cassess.fulton.asu.edu/cassess/#/";
            //var url = "http://localhost:8090/#/";

            $scope.currentCourse = course;

            $scope.admin = {};

            $scope.admins = [];

            $scope.message = "";

            if ($rootScope.coursePackage.course === course) {
                $scope.admins = $rootScope.coursePackage.admins;
            } else {
                window.location.href = url + 'create_course';
            }

            if(course === null){
                window.location.href = url + 'create_course';
            }

            $scope.setClickedAdmin = function(index){
                $scope.selectedRow = index;
                $scope.selectedAdmin = $scope.admins[index];
            };

            $scope.saveAdmin = function() {
                if ($scope.enteredEmail != null && $scope.enteredEmail != '') {
                    if ($scope.enteredName != null && $scope.enteredName != '') {
                        if ($scope.enteredPassword != null && $scope.enteredPassword != '') {
                            $scope.admin.email = $scope.enteredEmail;
                            $scope.admin.full_name = $scope.enteredName;
                            $scope.admin.password = $scope.enteredPassword;
                            var exists = false;
                            for (var i in $scope.admins) {
                                if($scope.admins[i].email ===  $scope.admin.email){
                                    $scope.admins[i] = $scope.admin;
                                    exists = true;
                                }
                            }
                            if(!exists) {
                                if(!$scope.admins || $scope.admins.length === 0){
                                    $scope.admins = [$scope.admin];
                                } else {
                                    $scope.admins.push($scope.admin);
                                }
                            }
                            $scope.clearAdminForm();
                            $scope.admin = {};
                            $scope.applyChanges();
                        } else {
                            $scope.message = 'Please Enter Password prior to saving an Admin';
                        }
                    } else {
                        $scope.message = 'Please Enter Name prior to saving an Admin';
                    }
                } else {
                    $scope.message = 'Please Enter Email prior to saving an Amdin';
                }
            };

            $scope.saveAdminsJSON = function(adminsArray) {
                if(adminsArray[0]) {
                    if (adminsArray[0].email) {
                        if (adminsArray[0].email != null && adminsArray[0].email != '') {
                            for (var i in adminsArray) {
                                $scope.enteredEmail = adminsArray[i].email;
                                $scope.enteredName = adminsArray[i].full_name;
                                $scope.enteredPassword = adminsArray[i].password;
                                $scope.saveAdmin();
                            }
                            $scope.$apply();
                        } else {
                            $scope.message = 'No Admin data in uploaded csv';
                        }
                    } else {
                        $scope.message = 'No Admin data in uploaded csv';
                    }
                } else {
                    $scope.message = 'No Admin data in uploaded csv';
                }
            };

            $scope.clearAdminForm = function() {
                $scope.enteredName = '';
                $scope.enteredEmail = '';
                $scope.enteredPassword = '';
            };

            $scope.removeAdmin = function() {
                for (var i in $scope.admins) {
                    var email = $scope.selectedAdmin.email;
                    if ($scope.admins[i].email === email) {
                        $scope.admins.splice(i, 1);
                        $scope.selectedRow = -1;
                    }
                }
            };

            $scope.editAdmin = function() {
                var email = $scope.selectedAdmin.email;
                for (var i in $scope.admins) {
                    if ($scope.admins[i].email === email) {
                        $scope.admin = angular.copy($scope.admins[i]);
                    }
                }
                $scope.enteredName = $scope.admin.full_name;
                $scope.enteredEmail = $scope.admin.email;
                $scope.enteredPassword = $scope.admin.password;
            };

            $scope.applyChanges = function() {
                $scope.message = 'Successfully saved to course ' + $rootScope.coursePackage.course;
                $rootScope.coursePackage.admins = $scope.admins;
            };

            // CSV Reading Functions---------------------------------
            $scope.handleFiles = function(files) {
                if (window.FileReader) {
                    $scope.getAsText(files[0]);
                } else {
                    alert('FileReader is not supported in this browser.');
                }
            };

            $scope.getAsText = function(fileToRead) {
                var reader = new FileReader();
                reader.onload = $scope.loadHandler;
                reader.onerror = $scope.errorHandler;
                reader.readAsText(fileToRead);
            };

            $scope.loadHandler = function(event) {
                var csv = event.target.result;
                $scope.processData(csv);
            };

            $scope.processData = function(csv) {
                var csvArray = $scope.CSV2JSON(csv);
                $scope.saveAdminsJSON(JSON.parse(csvArray));
            };

            $scope.errorHandler = function(evt) {
                if(evt.target.error.name == "NotReadableError") {
                    alert("Cannot read file !");
                }
            };

            function CSVToArray(strData, strDelimiter) {
                strDelimiter = (strDelimiter || ",");
                var objPattern = new RegExp((
                "(\\" + strDelimiter + "|\\r?\\n|\\r|^)" +
                "(?:\"([^\"]*(?:\"\"[^\"]*)*)\"|" +
                "([^\"\\" + strDelimiter + "\\r\\n]*))"), "gi");
                var arrData = [[]];
                var arrMatches = null;
                while (arrMatches = objPattern.exec(strData)) {
                    var strMatchedDelimiter = arrMatches[1];
                    if (strMatchedDelimiter.length && (strMatchedDelimiter != strDelimiter)) {
                        arrData.push([]);
                    }
                    if (arrMatches[2]) {
                        var strMatchedValue = arrMatches[2].replace(
                            new RegExp("\"\"", "g"), "\"");
                    } else {
                        var strMatchedValue = arrMatches[3];
                    }
                    arrData[arrData.length - 1].push(strMatchedValue);
                }
                return (arrData);
            }
            $scope.CSV2JSON = function(csv) {
                var array = CSVToArray(csv);
                var objArray = [];
                for (var i = 1; i < array.length; i++) {
                    objArray[i - 1] = {};
                    for (var k = 0; k < array[0].length && k < array[i].length; k++) {
                        var key = array[0][k];
                        objArray[i - 1][key] = array[i][k]
                    }
                }
                var json = JSON.stringify(objArray);
                var str = json.replace(/},/g, "},\r\n");
                return str;
            }

        }])
    .controller("newCourseTeams", ['$rootScope', '$scope', '$http', '$window', 'provisionService', 'courseCreateService',
        function ($rootScope, $scope, $http, $window, provisionService, courseCreateService) {

            $scope.gitHubVerified = false;

            var fromCSV = false;

            var isEdit = false;

            $rootScope.provisionMode = true;

            $scope.tab = 3;

            $scope.setTab = function (newTab) {
                $scope.tab = newTab;
            };

            $scope.isSet = function (tabNum) {
                return $scope.tab === tabNum;
            };

            var course = courseCreateService.getCourse();

            $scope.currentCourse = course;

            $scope.team = {};

            $scope.teams = [];

            $scope.message = "";

            var gitHubInfoChanged = false;

            var taigaInfoChanged = false;

            var url = "http://cassess.fulton.asu.edu/cassess/#/";
            //var url = "http://localhost:8090/#/";

            if ($rootScope.coursePackage.course === course) {
                $scope.teams = $rootScope.coursePackage.teams;
                for (var i in $rootScope.coursePackage.teams){
                    //console.log("Team: " + $rootScope.coursePackage.teams[i].team_name);
                    //console.log("TaigaHideOk: " + $rootScope.coursePackage.teams[i].hideTaigaOk);
                    //console.log("GitHubHideOk: " + $rootScope.coursePackage.teams[i].hideGitHubOk);
                    //console.log("TaigaHideRemove: " + $rootScope.coursePackage.teams[i].hideTaigaRemove);
                    //console.log("GitHubHideRemove: " + $rootScope.coursePackage.teams[i].hideGitHubRemove);
                }
            } else {
                window.location.href = url + 'create_course';
            }

            if(course === null){
                window.location.href = url + 'create_course';
            }

            $scope.setClickedTeam = function(index){
                $scope.selectedRow = index;
                $scope.selectedTeam = $scope.teams[index];
            };

            $scope.saveTeam = function() {
                if($scope.enteredTeamName != null && $scope.enteredTeamName != ''){
                    if($scope.enteredTaigaSlug != null && $scope.enteredTaigaSlug != ''){
                        if ($scope.enteredGitHubOwner != null && $scope.enteredGitHubOwner != '') {
                            if ($scope.enteredGitHubToken != null && $scope.enteredGitHubToken != '') {
                                if($scope.enteredGitHubRepo != null && $scope.enteredGitHubRepo != ''){
                                    if($scope.taigaVerified === true){
                                        $scope.team.hideTaigaOk = false;
                                        $scope.team.hideTaigaRemove = true;
                                    } else {
                                        $scope.team.hideTaigaOk = true;
                                        $scope.team.hideTaigaRemove = false;
                                    }
                                    if($scope.gitHubVerified === true){
                                        $scope.team.hideGitHubOk = false;
                                        $scope.team.hideGitHubRemove = true;
                                    } else {
                                        $scope.team.hideGitHubOk = true;
                                        $scope.team.hideGitHubRemove = false;
                                    }
                                    $scope.team.team_name = $scope.enteredTeamName;
                                    $scope.team.taiga_project_slug = $scope.enteredTaigaSlug;
                                    $scope.team.github_owner = $scope.enteredGitHubOwner;
                                    $scope.team.github_token = $scope.enteredGitHubToken;
                                    $scope.team.github_repo_id = $scope.enteredGitHubRepo;
                                    $scope.team.slack_team_id = $scope.enteredSlackTeam;
                                    var exists = false;
                                    if ($scope.teams != null) {
                                        for (var i in $scope.teams) {
                                            if($scope.teams[i].team_name === $scope.team.team_name){
                                                $scope.teams[i] = $scope.team;
                                                exists = true;
                                            }
                                        }
                                        if(!exists) {
                                            if($scope.teams.length == 0){
                                                $scope.teams = [$scope.team];
                                            } else {
                                                $scope.teams.push($scope.team);
                                            }
                                        }
                                    } else {
                                        $scope.teams = [$scope.team]
                                    }
                                    $scope.clearTeamForm();
                                    $scope.team = {};
                                    $scope.applyChanges();
                                } else {
                                    $scope.message = 'Please Enter GitHub Repo prior to saving a Team';
                                }
                            } else {
                                $scope.message = 'Please Enter Github token prior to saving a Team';
                            }
                        } else {
                            $scope.message = 'Please Enter GitHub owner prior to saving a Team';
                        }
                    } else {
                        $scope.message = 'Please Enter Taiga Slug prior to saving a Team';
                    }
                } else {
                    $scope.message = 'Please Enter Team Name prior to saving a Team';
                }
            };

            $scope.saveTeamsJSON = function(teamsArray) {
                if(teamsArray[0]){
                    if(teamsArray[0].team_name){
                        if(teamsArray[0].team_name != null && teamsArray[0].team_name != ''){
                            for (var i in teamsArray) {
                                fromCSV = true;
                                //console.log(teamsArray[i].team_name);
                                $scope.enteredTeamName = teamsArray[i].team_name;
                                $scope.enteredTaigaSlug = teamsArray[i].taiga_project_slug;
                                $scope.enteredGitHubOwner = teamsArray[i].github_owner;
                                $scope.enteredGitHubToken = teamsArray[i].github_token;
                                $scope.enteredGitHubRepo = teamsArray[i].github_repo_id;
                                $scope.enteredSlackTeam = teamsArray[i].slack_team_id;
                                $scope.saveTeam();
                            }
                            $scope.$apply();
                        } else {
                            $scope.message = 'No Team data in uploaded csv';
                        }
                    } else {
                        $scope.message = 'No Team data in uploaded csv';
                    }
                } else {
                    $scope.message = 'No Team data in uploaded csv';
                }

            };

            $scope.removeTeam = function() {
                for (var i in $scope.teams) {
                    var team_name = $scope.selectedTeam.team_name;
                    if ($scope.teams[i].team_name === team_name) {
                        $scope.teams.splice(i, 1);
                        $scope.selectedRow = -1;
                    }
                }
            };

            $scope.clearTeamForm = function() {
                fromCSV = false;
                $scope.enteredTeamName = '';
                $scope.enteredTaigaSlug = '';
                $scope.enteredGitHubOwner = '';
                $scope.enteredGitHubToken = '';
                $scope.enteredGithubRepo = '';
                $scope.enteredSlackTeam = '';
                $scope.taigaVerified = false;
                $scope.taigaNotVerified = false;
                $scope.gitHubVerified = false;
                $scope.gitHubNotVerified = false;
                gitHubInfoChanged = false;
                taigaInfoChanged = false;
            };

            $scope.editTeam = function() {
                isEdit = true;
                fromCSV = false;
                var team_name = $scope.selectedTeam.team_name;
                for (var i in $scope.teams) {
                    if ($scope.teams[i].team_name === team_name) {
                        $scope.team = angular.copy($scope.teams[i]);
                    }
                }
                $scope.enteredTeamName = $scope.team.team_name;
                $scope.enteredTaigaSlug = $scope.team.taiga_project_slug;
                $scope.enteredGitHubOwner = $scope.team.github_owner;
                $scope.enteredGitHubToken = $scope.team.github_token;
                $scope.enteredGitHubRepo = $scope.team.github_repo_id;
                $scope.enteredSlackTeam =  $scope.team.slack_team_id;
                if($scope.team.hideGitHubOk === false && $scope.team.hideGitHubRemove === true){
                    $scope.gitHubVerified = true;
                    $scope.gitHubNotVerified = false;
                } else if($scope.team.hideGitHubOk === true && $scope.team.hideGitHubRemove === false){
                    $scope.gitHubVerified = false;
                    $scope.gitHubNotVerified = true;
                }
                if($scope.team.hideTaigaOk === false && $scope.team.hideTaigaRemove === true){
                    $scope.taigaVerified = true;
                    $scope.taigaNotVerified = false;
                } else if($scope.team.hideTaigaOk === true && $scope.team.hideTaigaRemove === false){
                    $scope.taigaVerified = false;
                    $scope.taigaNotVerified = true;
                }
                gitHubInfoChanged = false;
                taigaInfoChanged = false;
            };

            $scope.applyChanges = function() {
                $scope.message = 'Successfully saved to course ' + $rootScope.coursePackage.course;
                $rootScope.coursePackage.teams = $scope.teams;
            };


            // CSV Reading Functions---------------------------------
            $scope.handleFiles = function(files) {
                if (window.FileReader) {
                    $scope.getAsText(files[0]);
                } else {
                    alert('FileReader is not supported in this browser.');
                }
            };

            $scope.getAsText = function(fileToRead) {
                var reader = new FileReader();
                reader.onload = $scope.loadHandler;
                reader.onerror = $scope.errorHandler;
                reader.readAsText(fileToRead);
            };

            $scope.loadHandler = function(event) {
                var csv = event.target.result;
                $scope.processData(csv);
            };

            $scope.processData = function(csv) {
                var csvArray = $scope.CSV2JSON(csv);
                $scope.saveTeamsJSON(JSON.parse(csvArray));
            };


            $scope.errorHandler = function(evt) {
                if(evt.target.error.name == "NotReadableError") {
                    alert("Cannot read file !");
                }
            };

            function CSVToArray(strData, strDelimiter) {
                strDelimiter = (strDelimiter || ",");
                var objPattern = new RegExp((
                "(\\" + strDelimiter + "|\\r?\\n|\\r|^)" +
                "(?:\"([^\"]*(?:\"\"[^\"]*)*)\"|" +
                "([^\"\\" + strDelimiter + "\\r\\n]*))"), "gi");
                var arrData = [[]];
                var arrMatches = null;
                while (arrMatches = objPattern.exec(strData)) {
                    var strMatchedDelimiter = arrMatches[1];
                    if (strMatchedDelimiter.length && (strMatchedDelimiter != strDelimiter)) {
                        arrData.push([]);
                    }
                    if (arrMatches[2]) {
                        var strMatchedValue = arrMatches[2].replace(
                            new RegExp("\"\"", "g"), "\"");
                    } else {
                        var strMatchedValue = arrMatches[3];
                    }
                    arrData[arrData.length - 1].push(strMatchedValue);
                }
                return (arrData);
            }
            $scope.CSV2JSON = function(csv) {
                var array = CSVToArray(csv);
                var objArray = [];
                for (var i = 1; i < array.length; i++) {
                    objArray[i - 1] = {};
                    for (var k = 0; k < array[0].length && k < array[i].length; k++) {
                        var key = array[0][k];
                        objArray[i - 1][key] = array[i][k]
                    }
                }
                var json = JSON.stringify(objArray);
                var str = json.replace(/},/g, "},\r\n");
                return str;
            }

            $scope.verifyGitHub = function () {

                if ($scope.enteredGitHubOwner != '' || $scope.enteredGitHubOwner != null){
                    if($scope.enteredGitHubRepo != '' || $scope.enteredGitHubRepo != null) {
                        if ($scope.enteredGitHubToken  != '' || $scope.enteredGitHubToken != null) {
                            var gitHubUrl = "https://api.github.com/repos/" + $scope.enteredGitHubOwner + "/"
                                + $scope.enteredGitHubRepo + "/stats/contributors?access_token=" + $scope.enteredGitHubToken
                                + "&scope=&token_type=bearer";
                            $http({
                                url: gitHubUrl,
                                method: "GET",
                                headers: {'Content-Type': 'application/json; charset=utf-8'}
                            }).then(function (response) {
                                //console.log("Worked!");
                                $scope.message = "GitHub Success: " + response.status;
                                $scope.gitHubNotVerified = false;
                                $scope.gitHubVerified = true;
                                gitHubInfoChanged = false;
                            }, function (response) {
                                //console.log("didn't work");
                                $scope.message = "GitHub Failure: " + response.status;
                                $scope.gitHubVerified = false;
                                $scope.gitHubNotVerified = true;
                            });
                        } else {
                            $scope.message = 'Please enter GitHub Token prior to attempting verify';
                        }
                    } else {
                        $scope.message = 'Please enter GitHub Repo prior to attempting verify';
                    }
                } else {
                    $scope.message = 'Please enter GitHub Owner prior to attempting verify';
                }

            };

            $scope.verifyTaiga = function () {

                if ($rootScope.coursePackage.taiga_token != '' || $rootScope.coursePackage.taiga_token != null){
                        if ($scope.enteredTaigaSlug  != '' || $scope.enteredTaigaSlug != null) {
                            var taigaUrl = "https://api.taiga.io/api/v1/projects/by_slug?slug=" + $scope.enteredTaigaSlug;
                            $http({
                                url: taigaUrl,
                                method: "GET",
                                headers: {'Authorization': 'Bearer ' + $rootScope.coursePackage.taiga_token,
                                    'Content-Type': 'application/json; charset=utf-8'}
                            }).then(function (response) {
                                //console.log("Worked!");
                                $scope.message = "Taiga Success: " + response.status;
                                $scope.taigaNotVerified = false;
                                $scope.taigaVerified = true;
                                taigaInfoChanged = false;
                            }, function (response) {
                                //console.log("didn't work");
                                $scope.message = "Taiga Failure: " + response.status;
                                $scope.taigaVerified = false;
                                $scope.taigaNotVerified = true;
                            });
                        } else {
                            $scope.message = 'Please enter Taiga Slug prior to attempting verify';
                        }
                } else {
                    $scope.message = 'Please enter Course Taiga Token prior to attempting verify';
                }

            };

            $scope.setGitHubUnverified = function(){
                if(!gitHubInfoChanged) {
                    $scope.gitHubVerified = false;
                    $scope.gitHubNotVerified = true;
                    gitHubInfoChanged = true;
                }
            };

            $scope.setTaigaUnverified = function() {
                if (!taigaInfoChanged) {
                    $scope.taigaVerified = false;
                    $scope.taigaNotVerified = true;
                    taigaInfoChanged = true;
                }
            };

        }])
    .controller("newCourse", ['$rootScope', '$scope', '$http', '$window', 'provisionService', 'courseCreateService',
        function ($rootScope, $scope, $http, $window, provisionService, courseCreateService) {

            $rootScope.provisionMode = true;

            $scope.tab = 1;

            $scope.setTab = function (newTab) {
                $scope.tab = newTab;
            };

            $scope.isSet = function (tabNum) {
                return $scope.tab === tabNum;
            };

            $scope.message = "";

            $scope.course = [];

            var taigaTokenChanged = false;

            var n =  new Date();
            var y = n.getFullYear();
            var m = n.getMonth() + 1;
            var d = n.getDate();
            $scope.minDate = n;

            //console.log("minDate: " + $scope.minDate);

            $rootScope.provisionMode = true;
            if($rootScope.coursePackage.course != null && $rootScope.coursePackage.course != '')   {
                courseCreateService.setCourse($rootScope.coursePackage.course);
                //console.log("setCourse: " + $rootScope.coursePackage.course);
            }

            $scope.saveCourse = function() {
                if($scope.enteredCourseName != null && $scope.enteredCourseName != '') {
                    if ($scope.enteredEndDate != null && $scope.enteredEndDate != '') {
                                if ($scope.enteredTaigaToken != null && $scope.enteredTaigaToken != '') {
                                    $rootScope.coursePackage.course = $scope.enteredCourseName;
                                    var dateEntered = new Date($scope.enteredEndDate);
                                    dateEntered.setDate(dateEntered.getDate());
                                    var dateConverted = (dateEntered.getFullYear() + '-' + ('0' + (dateEntered.getMonth()+1)).slice(-2) + '-' + ('0' + dateEntered.getDate()).slice(-2))
                                    $rootScope.coursePackage.end_date = dateConverted;
                                    if ($scope.enteredTaigaToken != $scope.coursePackage.taiga_token) {
                                        for (var i in $rootScope.coursePackage.teams) {
                                            $rootScope.coursePackage.teams[i].hideTaigaOk = true;
                                            $rootScope.coursePackage.teams[i].hideTaigaRemove = false;
                                        }
                                    }
                                    $rootScope.coursePackage.taiga_token = $scope.enteredTaigaToken;
                                    $rootScope.coursePackage.slack_token = $scope.enteredSlackToken;
                                    courseCreateService.setCourse($rootScope.coursePackage.course);
                                    //console.log("saveCourse: " + $rootScope.coursePackage.course);
                                    $scope.message = 'Course ' + $scope.enteredCourseName + ' successfuly saved';
                                    $scope.clearCourseForm();
                                } else {
                                    $scope.message = 'Please Enter Taiga token prior to saving a Course';
                                }
                    } else {
                        $scope.message = 'Please Enter End date prior to saving a Course';
                    }
                } else {
                    $scope.message = 'Please Enter Course name prior to saving a Course';
                }
            };

            $scope.editCourse = function() {
                $scope.enteredCourseName = $scope.coursePackage.course;
                var returnedDate = new Date($scope.coursePackage.end_date);
                returnedDate.setDate(returnedDate.getDate() + 1);
                $scope.enteredEndDate = returnedDate;
                $scope.enteredTaigaToken = $scope.coursePackage.taiga_token;
                $scope.enteredSlackToken = $scope.coursePackage.slack_token;
            };

            $scope.clearCourseForm = function() {
                $scope.enteredCourseName = '';
                $scope.enteredEndDate = '';
                $scope.enteredTaigaToken = '';
                $scope.enteredSlackToken = '';
                taigaTokenChanged = false;
            };

            $scope.removeCourse = function() {
                $rootScope.coursePackage = {
                    "admins": null,
                    "course": null,
                    "end_date": '',
                    "slack_token": '',
                    "taiga_token": '',
                    "teams": null
                    //"teams": [{
                    //"taiga_project_slug": '',
                    //"team_name": '',
                    //"channels": null,
                    //"github_repo_id": '',
                    //"slack_team_id": '',
                    //"students": null
                    //}]
                };
                $scope.clearCourseForm();
            };

            $scope.localStore = function () {
                var teamsCount = 0;

                var studentsCount = 0;

                var adminsCount = 0;

                delete $rootScope.coursePackage.$$hashKey;

                if($rootScope.coursePackage.admins){
                    for (var i in $rootScope.coursePackage.admins) {
                        adminsCount += 1;
                        delete $rootScope.coursePackage.admins[i].$$hashKey;
                    }
                }

                if ($rootScope.coursePackage.teams) {
                    for (var i in $rootScope.coursePackage.teams) {
                        teamsCount += 1;
                        delete $rootScope.coursePackage.teams[i].$$hashKey;
                        if($rootScope.coursePackage.teams[i].students){
                            for (var j in $rootScope.coursePackage.teams[i].students) {
                                studentsCount += 1;
                                delete $rootScope.coursePackage.teams[i].students[j].$$hashKey;
                            }
                        }
                    }
                }

                if ($rootScope.coursePackage.teams) {
                    for (var i in $rootScope.coursePackage.teams) {
                        delete $rootScope.coursePackage.teams[i].$$hashKey;
                        if($rootScope.coursePackage.teams[i].slack_team_id) {
                            if ($rootScope.coursePackage.teams[i].slack_team_id == '' || $rootScope.coursePackage.teams[i].slack_team_id == null) {
                                delete $rootScope.coursePackage.teams[i].slack_team_id;
                            }
                        }
                        if ($rootScope.coursePackage.teams[i].channels) {
                            for (var j in $rootScope.coursePackage.teams[i].channels) {
                                delete $rootScope.coursePackage.teams[i].channels[j].$$hashKey;
                            }
                        }
                    }
                }

                if ($rootScope.coursePackage.slack_token) {
                    if($rootScope.coursePackage.slack_token == '' || $rootScope.coursePackage.slack_token == null){
                        delete $rootScope.coursePackage.slack_token;
                    }
                }

                if(adminsCount > 0) {
                    if (teamsCount > 0) {
                        if (studentsCount > 0) {
                            $window.localStorage.setItem('storedCoursePackage', JSON.stringify($rootScope.coursePackage));
                            $scope.message = 'Course ' + $rootScope.coursePackage.course + ' successfully stored';
                        } else {
                            $scope.message = 'Please create at least one student prior to attempting save';
                        }
                    } else {
                        $scope.message = 'Please create at least one team prior to attempting save';
                    }
                } else {
                    $scope.message = 'Please create at least one admin prior to attempting save';
                }

            };

            $scope.localGet = function () {
                if($window.localStorage.getItem('storedCoursePackage')) {
                    var json = JSON.parse($window.localStorage.getItem('storedCoursePackage'));
                    if (json.course) {
                        if (json.course != null && json.course != '') {
                            $rootScope.coursePackage = json;
                            courseCreateService.setCourse($rootScope.coursePackage.course);
                            $scope.message = 'Course ' + $rootScope.coursePackage.course + ' successfully retrieved';
                        } else {
                            $scope.message = 'No course data in local storage';
                        }
                    } else {
                        $scope.message = 'No course data in local storage';
                    }
                }
            };

            // JSON Reading Functions---------------------------------
            $scope.handleFiles = function(files) {
                if (window.FileReader) {
                    $scope.getAsText(files[0]);
                } else {
                    alert('FileReader are not supported in this browser.');
                }
            };

            $scope.getAsText = function(fileToRead) {
                var reader = new FileReader();
                reader.onload = $scope.loadHandler;
                reader.onerror = $scope.errorHandler;
                reader.readAsText(fileToRead);
            };

            $scope.loadHandler = function(event) {
                var json = event.target.result;
                $scope.processData(json);
            };

            $scope.processData = function(json) {
                //console.log(JSON.parse(json));
                //console.log(json);
                try {
                    var coursePackage = JSON.parse(json);
                    if (coursePackage.course) {
                        if (coursePackage.course != null && coursePackage.course != '') {
                            $rootScope.coursePackage = coursePackage;
                            courseCreateService.setCourse($rootScope.coursePackage.course);
                            $scope.message = 'Course ' + $rootScope.coursePackage.course + ' successfully uploaded via JSON';
                        } else {
                            $scope.message = 'No course data in uploaded JSON';
                        }
                    } else {
                        $scope.message = 'No course data in uploaded JSON';
                    }
                } catch (err){
                    $scope.message = "Exception while applying json to course: " + err.message;
                }
                $scope.$apply();
            };

            $scope.errorHandler = function(evt) {
                if(evt.target.error.name == "NotReadableError") {
                    alert("Cannot read file !");
                }
            };

            $scope.saveJSON = function () {

                var data = $rootScope.coursePackage;

                if($rootScope.coursePackage.course){
                    if($rootScope.coursePackage.course != null && $rootScope.coursePackage.course != ''){
                        var filename = 'coursePackage' + $rootScope.coursePackage.course + '.json';

                        if (!data) {
                            console.error('No data');
                            return;
                        }

                        if (typeof data === 'object') {
                            data = JSON.stringify(data, undefined, 2);
                        }

                        var blob = new Blob([data], {type: 'text/json'});

                        // FOR IE:

                        if (window.navigator && window.navigator.msSaveOrOpenBlob) {
                            window.navigator.msSaveOrOpenBlob(blob, filename);
                        }
                        else{
                            var e = document.createEvent('MouseEvents'),
                                a = document.createElement('a');

                            a.download = filename;
                            a.href = window.URL.createObjectURL(blob);
                            a.dataset.downloadurl = ['text/json', a.download, a.href].join(':');
                            e.initEvent('click', true, false, window,
                                0, 0, 0, 0, 0, false, false, false, false, 0, null);
                            a.dispatchEvent(e);
                            $scope.message = filename + ' successfully downloaded';
                        }
                    } else{
                        $scope.message = 'Please create a course prior to attempting JSON download'
                    }
                } else{
                    $scope.message = 'Please create a course prior to attempting JSON download'
                }
            };

        }])
    .controller("newCourseChannels", ['$rootScope', '$scope', '$http', '$window', '$timeout', 'provisionService', 'courseCreateService', 'teamCreateService',
        function ($rootScope, $scope, $http, $window, $timeout, provisionService, courseCreateService, teamCreateService) {

            $rootScope.provisionMode = true;

            var course = courseCreateService.getCourse();
            //console.log("GetCourse: " + course);

            $scope.currentCourse = course;

            var teamIndex = -1;

            var fromCSV = false;

            var nextIndex = -1;

            var url = "http://cassess.fulton.asu.edu/cassess/#/";
            //var url = "http://localhost:8090/#/";

            $scope.channel = {};

            $scope.channels = [];

            $scope.teams = [];

            $scope.message = "";

            $scope.currentTeam = "";

            $scope.teamsArray = [];

            if ($rootScope.coursePackage.course === course) {
                if($rootScope.coursePackage.teams){
                    if($rootScope.coursePackage.teams.length != 0) {
                        teamIndex = 0;
                        $scope.teams = $rootScope.coursePackage.teams;
                        for (var i in $scope.teams) {
                            $scope.teamsArray.push($scope.teams[i].team_name);
                            //console.log($scope.teams[i].team_name);
                        }

                    } else {
                        window.location.href = url + 'create_teams';
                    }
                } else {
                    window.location.href = url + 'create_teams';
                }
            } else {
                window.location.href = url + 'create_course';
            }
            if (teamIndex == -1){
                window.location.href = url + 'create_teams';
            }
            if(course === null){
                window.location.href = url + 'create_course';
            }

            $scope.tab = 5;

            $scope.setTab = function (newTab) {
                $scope.tab = newTab;
            };

            $scope.isSet = function (tabNum) {
                return $scope.tab === tabNum;
            };

            $scope.setTeam = function() {
                for (var i in $rootScope.coursePackage.teams) {
                    if ($rootScope.coursePackage.teams[i].team_name === $scope.selectedTeam.team_name) {
                        $scope.channels = $rootScope.coursePackage.teams[i].channels;
                        for (var j in $rootScope.coursePackage.teams[i].channels)   {
                            if(!$rootScope.coursePackage.teams[i].channels){
                                $rootScope.coursePackage.teams[i].channels = [];
                            } else {
                                console.log("Current Channel: " + $rootScope.coursePackage.teams[i].channels[j].id);
                            }
                        }
                        teamIndex = i;
                        //console.log("Team Index: " + i);
                        teamCreateService.setTeam($rootScope.coursePackage.teams[i].team_name);
                        $scope.currentTeam = $scope.selectedTeam.team_name;
                    }
                }
            };

            $scope.saveChannel = function() {
                if($scope.currentTeam != ""){
                    if($scope.enteredId != null && $scope.enteredId != ''){
                        $scope.channel.id = $scope.enteredId;
                        var exists = false;
                        for (var i in $scope.channels) {
                            if($scope.channels[i].id === $scope.channel.id){
                                $scope.channels[i] = $scope.channel;
                                exists = true;
                            }
                        }
                        if(!exists) {
                            if(!$scope.channels || $scope.channels.length === 0){
                                $scope.channels = [$scope.channel];
                            } else {
                                $scope.channels.push($scope.channel);
                            }
                        }
                        $scope.clearChannelForm();
                        $scope.channel = {};
                        if(!fromCSV || nextIndex != teamIndex){
                            $scope.applyChanges();
                        }
                    } else {
                        $scope.message = 'Please Enter Id prior to saving a channel';
                    }
                } else {
                    $scope.message = 'Please Select a team prior to saving a channel';
                }
            };

            $scope.saveChannelsJSON = function(channelsArray) {
                fromCSV = true;
                if(channelsArray[0]) {
                    if (channelsArray[0].id) {
                        if (channelsArray[0].id != null && channelsArray[0].id != '') {
                            var lastIndex = -1;
                            var j = 0;
                            for (var i in channelsArray) {
                                var arrayIndex = $scope.teamsArray.indexOf(channelsArray[i].team_name);
                                ++j;
                                //console.log("i value: " + i);
                                //console.log("next i value: " + j);
                                if(channelsArray[j]){
                                    if(channelsArray[j].team_name){
                                        nextIndex = $scope.teamsArray.indexOf(channelsArray[j].team_name);
                                        //console.log("NextTeam: " + channelsArray[j].team_name + " at index: " + nextIndex);
                                    } else {
                                        nextIndex = -1;
                                    }
                                } else {
                                    nextIndex = -1;
                                }
                                if(arrayIndex >= 0){
                                    teamIndex = arrayIndex;
                                    if(arrayIndex != lastIndex){
                                        $scope.channels = $rootScope.coursePackage.teams[teamIndex].channels;
                                    }
                                    $scope.selectedTeam = $scope.teams[teamIndex];
                                    $scope.enteredId = channelsArray[i].id;
                                    $scope.currentTeam = $scope.selectedTeam.team_name;
                                    $scope.saveChannel();
                                    $scope.$apply();
                                    lastIndex = arrayIndex;
                                } else {
                                    alert(channelsArray[i].team_name + ' does not exist');
                                }
                            }
                        } else {
                            $scope.message = 'No Channel data in uploaded csv';
                        }
                    } else {
                        $scope.message = 'No Channel data in uploaded csv';
                    }
                } else {
                    $scope.message = 'No Channel data in uploaded csv';
                }
                fromCSV = false;
            };

            $scope.clearChannelForm = function() {
                $scope.enteredId = '';
            };

            $scope.editChannel = function() {
                var id = $scope.selectedChannel.id;
                for (var i in $scope.channels) {
                    if ($scope.channels[i].id === id) {
                        $scope.channel = angular.copy($scope.channels[i]);
                    }
                }
                $scope.enteredId = $scope.channel.id;
            };

            $scope.removeChannel = function() {
                for (var i in $scope.channels) {
                    var id = $scope.selectedChannel.id;
                    if ($scope.channels[i].id === id) {
                        $scope.channels.splice(i, 1);
                        $scope.selectedRow = -1;
                    }
                }
            };

            $scope.applyChanges = function() {
                if ($scope.selectedTeam.team_name) {
                    if($scope.selectedTeam.team_name != null || $scope.selectedTeam.team_name != ''){
                        //console.log("Applying Changes for team " + $scope.selectedTeam.team_name + " at index " + teamIndex);
                        $rootScope.coursePackage.teams[teamIndex].channels = $scope.channels;
                        $scope.message = "Successfully saved to " + $rootScope.coursePackage.teams[teamIndex].team_name;
                    } else {
                        $scope.message = "Please first select a Team to save Channel to"
                    }
                } else {
                    $scope.message = "Please first select a Team to save Channel to"
                }

            };

            // CSV Reading Functions---------------------------------
            $scope.handleFiles = function(files) {
                if (window.FileReader) {
                    $scope.getAsText(files[0]);
                } else {
                    alert('FileReader is not supported in this browser.');
                }
            };

            $scope.getAsText = function(fileToRead) {
                var reader = new FileReader();
                reader.onload = $scope.loadHandler;
                reader.onerror = $scope.errorHandler;
                reader.readAsText(fileToRead);
            };

            $scope.loadHandler = function(event) {
                var csv = event.target.result;
                $scope.processData(csv);
            };

            $scope.processData = function(csv) {
                var csvArray = $scope.CSV2JSON(csv);
                //console.log(csvArray);
                //console.log(JSON.parse(csvArray));
                $scope.saveChannelsJSON(JSON.parse(csvArray));
            };


            $scope.errorHandler = function(evt) {
                if(evt.target.error.name == "NotReadableError") {
                    alert("Cannot read file !");
                }
            };

            function CSVToArray(strData, strDelimiter) {
                strDelimiter = (strDelimiter || ",");
                var objPattern = new RegExp((
                "(\\" + strDelimiter + "|\\r?\\n|\\r|^)" +
                "(?:\"([^\"]*(?:\"\"[^\"]*)*)\"|" +
                "([^\"\\" + strDelimiter + "\\r\\n]*))"), "gi");
                var arrData = [[]];
                var arrMatches = null;
                while (arrMatches = objPattern.exec(strData)) {
                    var strMatchedDelimiter = arrMatches[1];
                    if (strMatchedDelimiter.length && (strMatchedDelimiter != strDelimiter)) {
                        arrData.push([]);
                    }
                    if (arrMatches[2]) {
                        var strMatchedValue = arrMatches[2].replace(
                            new RegExp("\"\"", "g"), "\"");
                    } else {
                        var strMatchedValue = arrMatches[3];
                    }
                    arrData[arrData.length - 1].push(strMatchedValue);
                }
                return (arrData);
            }
            $scope.CSV2JSON = function(csv) {
                var array = CSVToArray(csv);
                var objArray = [];
                for (var i = 1; i < array.length; i++) {
                    objArray[i - 1] = {};
                    for (var k = 0; k < array[0].length && k < array[i].length; k++) {
                        var key = array[0][k];
                        objArray[i - 1][key] = array[i][k]
                    }
                }
                var json = JSON.stringify(objArray);
                var str = json.replace(/},/g, "},\r\n");
                return str;
            }

        }])
    .controller("provisionCourse", ['$rootScope', '$scope', '$http', '$window', '$timeout', 'provisionService',
        function ($rootScope, $scope, $http, $window, $timeout, provisionService) {

            $rootScope.provisionMode = true;

            $scope.tab = 6;

            $scope.setTab = function (newTab) {
                $scope.tab = newTab;
            };

            $scope.isSet = function (tabNum) {
                return $scope.tab === tabNum;
            };

            $scope.message = "";

            var teamsCount = 0;

            var studentsCount = 0;

            var adminsCount = 0;

            delete $rootScope.coursePackage.$$hashKey;

            if($rootScope.coursePackage.admins){
                for (var i in $rootScope.coursePackage.admins) {
                    adminsCount += 1;
                    delete $rootScope.coursePackage.admins[i].$$hashKey;
                }
            }

            if ($rootScope.coursePackage.teams) {
                for (var i in $rootScope.coursePackage.teams) {
                    teamsCount += 1;
                    delete $rootScope.coursePackage.teams[i].$$hashKey;
                    if($rootScope.coursePackage.teams[i].students){
                        for (var j in $rootScope.coursePackage.teams[i].students) {
                            studentsCount += 1;
                            delete $rootScope.coursePackage.teams[i].students[j].$$hashKey;
                        }
                    }
                }
            }

            if ($rootScope.coursePackage.teams) {
                for (var i in $rootScope.coursePackage.teams) {
                    delete $rootScope.coursePackage.teams[i].$$hashKey;
                    if($rootScope.coursePackage.teams[i].slack_team_id) {
                        if ($rootScope.coursePackage.teams[i].slack_team_id == '' || $rootScope.coursePackage.teams[i].slack_team_id == null) {
                            delete $rootScope.coursePackage.teams[i].slack_team_id;
                        }
                    }
                    if ($rootScope.coursePackage.teams[i].channels) {
                        for (var j in $rootScope.coursePackage.teams[i].channels) {
                            delete $rootScope.coursePackage.teams[i].channels[j].$$hashKey;
                        }
                    }
                }
            }

            if ($rootScope.coursePackage.slack_token) {
                if($rootScope.coursePackage.slack_token == '' || $rootScope.coursePackage.slack_token == null){
                    delete $rootScope.coursePackage.slack_token;
                }
            }

            const jsonCoursePackage = angular.copy($rootScope.coursePackage);

            if (jsonCoursePackage.teams) {
                for (var i in jsonCoursePackage.teams) {
                    delete jsonCoursePackage.teams[i].hideTaigaOk;
                    delete jsonCoursePackage.teams[i].hideTaigaRemove;
                    delete jsonCoursePackage.teams[i].hideGitHubOk;
                    delete jsonCoursePackage.teams[i].hideGitHubRemove;
                }
            }

            $scope.jsonDisplay = jsonCoursePackage;

            $http.defaults.headers.post["Content-Type"] = "application/json; charset=utf-8";

            $scope.sendCoursePackage = function () {

                if (jsonCoursePackage.course){
                    if(adminsCount > 0) {
                        if (teamsCount > 0) {
                            if (studentsCount > 0) {
                                $http({
                                    url: './rest/coursePackage',
                                    method: "POST",
                                    data: jsonCoursePackage
                                }).then(function (response) {
                                    //console.log("Worked!");
                                    $scope.statusMessage = "Success: " + response.status;
                                    $scope.dataMessageJSON = response.data;
                                    $scope.otherMessage = false;
                                    $scope.jsonMessage = true;
                                }, function (response) {
                                    //console.log("didn't work");
                                    $scope.statusMessage = "Failure: " + response.status;
                                    $scope.dataMessageOther = response.data;
                                    $scope.jsonMessage = false;
                                    $scope.otherMessage = true;
                                });
                            } else {
                                $scope.message = 'Please create at least one student prior to attempting save';
                            }
                        } else {
                            $scope.message = 'Please create at least one team prior to attempting save';
                        }
                    } else {
                        $scope.message = 'Please create at least one admin prior to attempting save';
                    }
                } else {
                    $scope.message = 'Please create a course prior to attempting save';
                }

            };

            $scope.removeCourse = function () {

                if (jsonCoursePackage.course){
                    $http({
                        url: './rest/course',
                        method: "DELETE",
                        data: jsonCoursePackage,
                        headers: {'Content-Type': 'application/json; charset=utf-8'}
                    }).then(function (response) {
                        //console.log("Worked!");
                        $scope.statusMessage = "Success: " + response.status;
                        $scope.dataMessageJSON = response.data;
                        $scope.otherMessage = false;
                        $scope.jsonMessage = true;
                    }, function (response) {
                        //console.log("didn't work");
                        $scope.statusMessage = "Failure: " + response.status;
                        $scope.dataMessageOther = response.data;
                        $scope.jsonMessage = false;
                        $scope.otherMessage = true;
                    });
                } else {
                    $scope.message = 'Please enter/upload course data prior to attempting save';
                }
            };
        }]);


