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

    .controller('HomeController', function ($scope, HomeService) {
        $scope.technos = HomeService.getTechno();
    })
    .controller('UsersController', ['$scope', '$location', '$http', 'UsersService', function ($scope, $location, $http, UsersService) {
        $scope.users = UsersService.getAll();

        $scope.viewProfile = function (email, auth) {
            if (auth == 'student') {
                console.log("Email: " + email);
                console.log("Auth: " + auth);
                console.log("Path: /studentProfile/" + email);
                $location.path('/studentProfile/' + email);
            }
            if (auth == 'admin') {
                console.log("Email: " + email);
                console.log("Auth: " + auth);
                console.log("Path: /studentProfile/" + email);
                $location.path('/adminProfile/' + email);
            }
            if (auth == 'rest') {
                console.log("Email: " + email);
                console.log("Auth: " + auth);
                console.log("Path: /studentProfile/" + email);
                $location.path('/restProfile/' + email);
            }
        }
    }])
    .controller('StudentProfileController', ['$scope', '$location', '$routeParams', '$http', 'userService', '$window', function ($scope, $location, $routeParams, $http, userService, $window) {
        $scope.userid = $routeParams.user_id;

        $http({
            url: './current_user',
            method: "GET"
        }).then(function (response) {
            console.log("Worked!");
            console.log(response.data);
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
                console.log("Worked!");
                console.log(response.data);
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
                console.log("Worked!");
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
                console.log("Worked!");
                //console.log(response.data);
                $scope.courses = response.data;
            });
        }

        $scope.selectedCourseChanged = function () {
            $http({
                url: './student_teams',
                method: "GET",
                headers: {'course': $scope.studentCourse.value.course, 'email': $scope.userid}
            }).then(function (response) {
                $scope.course = $scope.studentCourse.value.course;
                //console.log(response.data);
                $scope.teams = response.data;
                console.log($scope.teams);
            }, function (response) {
                //fail case
                console.log("didn't work");
                //console.log(response);
            });
        }

        $scope.selectedTeamChanged = function () {
            //console.log(response.data);
            $scope.team = $scope.studentTeam.value.team;
            console.log($scope.team);
        }

        $scope.studentTeamRemove = function () {
            $http({
                url: './studentProfileDelTeam',
                method: "DELETE",
                headers: {
                    'course': $scope.studentCourse.value.course,
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
        }

        $scope.studentCourseRemove = function () {
            $http({
                url: './studentProfileDelCourse',
                method: "DELETE",
                headers: {'course': $scope.studentCourse.value.course, 'email': $scope.userid}
            }).then(function (response) {
                $scope.message = "Student Removed From Course";
                $window.alert($scope.message);
            }, function (response) {
                $scope.message = "Student Not Removed From Course";
                $window.alert($scope.message);
            });
        }

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
        }

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
    .controller('RestProfileController', ['$scope', '$location', '$routeParams', '$http', 'userService', '$window', function ($scope, $location, $routeParams, $http, userService, $window) {
        $scope.userid = $routeParams.user_id;

        $scope.password = {};

        $http({
            url: './current_user',
            method: "GET"
        }).then(function (response) {
            console.log("Worked!");
            console.log(response.data);
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
                console.log("Worked!");
                console.log(response.data);
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
                console.log("Worked!");
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
        }

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

    .controller('AdminProfileController', ['$scope', '$location', '$routeParams', '$http', 'userService', '$window', function ($scope, $location, $routeParams, $http, userService, $window) {
        $scope.userid = $routeParams.user_id;

        $http({
            url: './current_user',
            method: "GET"
        }).then(function (response) {
            console.log("Worked!");
            console.log(response.data);
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
                console.log("Worked!");
                console.log(response.data);
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
                console.log("Worked!");
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
                console.log("Worked!");
                console.log(response.data);
                $scope.courses = response.data;
            });
        }

        $scope.selectedCourseChanged = function () {
            $scope.course = $scope.adminCourse.value.course;
        }

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
        }

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
        }

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
    .controller('ApiDocController', function ($scope) {
        // init form
        $scope.isLoading = false;
        $scope.url = $scope.swaggerUrl = 'v2/api-docs';
        // error management
        $scope.myErrorHandler = function (data, status) {
            console.log('failed to load swagger: ' + status + '   ' + data);
        };

        $scope.infos = false;
    })
    .controller('TokensController', function ($scope, UsersService, TokensService, $q) {

        var browsers = ["Firefox", 'Chrome', 'Trident']

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
                $scope.message = "Oops! you have come to unauthorized page."
                break;
            case "404" :
                $scope.message = "Page not found."
                break;
            default:
                $scope.code = 500;
                $scope.message = "Oops! unexpected error"
        }

    })
    .controller('NavController', ['$scope', '$location', '$http', 'courseService', 'teamService', 'studentService', '$window', '$routeParams', 'userService',
        function ($scope, $location, $http, courseService, teamService, studentService, $window, $routeParams, userService) {

            $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";

            function getCourses(user) {
                if (userService.getAuth() == 'super_user') {
                    $http({
                        url: './taigaCourses',
                        method: "GET",
                    }).then(function (response) {
                        console.log("Worked!");
                        console.log(response.data);
                        $scope.courses = response.data;
                    });
                }
                if (userService.getAuth() == 'admin') {
                    $http({
                        url: './admin_courses',
                        method: "GET",
                        headers: {'email': user.email}
                    }).then(function (response) {
                        console.log("Worked!");
                        console.log(response.data);
                        $scope.courses = response.data;
                    });
                }
                if (userService.getAuth() == 'student') {
                    $http({
                        url: './student_courses',
                        method: "GET",
                        headers: {'email': user.email}
                    }).then(function (response) {
                        console.log("Worked!");
                        console.log(response.data);
                        $scope.courses = response.data;
                    });
                }

            }

            $http({
                url: './current_user',
                method: "GET"
            }).then(function (response) {
                console.log("Worked!");
                console.log(response.data);
                $scope.user = response.data;
                userService.setUser($scope.user.login);
                userService.setAuth($scope.user.authorities[0].name);
                getCourses($scope.user);
            });

            $scope.selectedCourseChanged = function (course) {
                if (userService.getAuth() == 'super_user') {
                    courseService.setCourse(course);
                    $http({
                        url: './course_teams',
                        method: "GET",
                        headers: {'course': course}
                    }).then(function (response) {
                        console.log("Worked!");
                        console.log(response.data);
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
                        console.log("Worked!");
                        console.log(response.data);
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
                        console.log("Worked!");
                        console.log(response.data);
                        $scope.teams = response.data;
                    });
                }
            }

            $scope.selectedTeamChanged = function (team) {
                if (userService.getAuth() == 'super_user') {
                    teamService.setTeam(team);
                    $http({
                        url: './team_students',
                        method: "GET",
                        headers: {'course': courseService.getCourse(), 'team': teamService.getTeam()}
                    }).then(function (response) {
                        console.log("Worked!");
                        console.log(response.data);
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
                        console.log("Worked!");
                        console.log(response.data);
                        $scope.students = response.data;
                    });
                }
                if (userService.getAuth() == 'student') {
                    teamService.setTeam(team);
                    console.log("EMail1: " + userService.getUser());
                    console.log("EMail2: " + $scope.user.login);
                    $http({
                        url: './student_data',
                        method: "GET",
                        headers: {
                            'email': $scope.user.login,
                            'course': courseService.getCourse(),
                            'team': teamService.getTeam()
                        }
                    }).then(function (response) {
                        console.log("Worked!");
                        console.log(response.data);
                        $scope.students = response.data;
                    });

                }

            }
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
                console.log("Role: " + $scope.selectedRole.value.name);
                console.log("Didn't work");
                //console.log(response);
                $scope.responseData = console.log(response.data);
                $scope.message = "User Not Registered, Duplicate User or Incorrect Information";
                $window.alert($scope.message);
            });

        };

    }])
    .controller('CourseController', ['$scope', '$routeParams', '$location', 'courseService', 'userService', '$http', function ($scope, $routeParams, $location, courseService, userService, $http) {
        if ($routeParams.course_id != null) {
            $scope.courseid = $routeParams.course_id;
        } else {
            $scope.courseid = "none";
        }
        console.log("course: " + $scope.courseid);

        $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";
        $http({
            url: './check_courseaccess',
            method: "GET",
            headers: {'course': $scope.courseid, 'login': userService.getUser(), 'auth': userService.getAuth()}
        }).then(function (response) {
            console.log("Worked!");
            console.log(response.data);
            if (response.data == false) {
                $location.path('/home');
            } else {
                getWeightFreq();
            }
        });

        function getWeightFreq() {
            $http({
                url: './taiga/course_weightFreq',
                method: "GET",
                headers: {'course': $scope.courseid}
            }).then(function (response) {
                console.log("Worked!");
                console.log(response.data);
                $scope.weightFreq = response.data;
                getTaigaActivity();
            });
        }

        function getTaigaActivity() {
            $http({
                url: './taiga/course_activity',
                method: "GET",
                headers: {'course': $scope.courseid}
            }).then(function (response) {
                console.log("Worked!");
                console.log(response.data);
                $scope.courseActivity = response.data;
                getTaigaIntervals();
            });
        }

        function getTaigaIntervals() {
            $http({
                url: './taiga/course_intervals',
                method: "GET",
                headers: {'course': $scope.courseid}
            }).then(function (response) {
                console.log("Worked!");
                console.log(response.data);
                $scope.courseIntervals = response.data;
            });
        }

        $scope.IntervalChangedBegin = function (rawWeekBeginning) {
            $scope.rawWeekBeginning = rawWeekBeginning;
            console.log("WeekBeginning: " + $scope.rawWeekBeginning);
            if ($scope.rawWeekEnding != null) {
                $http({
                    url: './taiga/course_tasks',
                    method: "GET",
                    headers: {
                        'course': $scope.courseid,
                        'weekBeginning': $scope.rawWeekBeginning,
                        'weekEnding': $scope.rawWeekEnding
                    }
                }).then(function (response) {
                    console.log("Worked!");
                    console.log(response.data);
                    $scope.courseTasks = response.data;
                });
            }
        }

        $scope.IntervalChangedEnd = function (rawWeekEnding) {
            $scope.rawWeekEnding = rawWeekEnding;
            console.log("WeekEnding: " + $scope.rawWeekEnding);
            if ($scope.rawWeekBeginning != null) {
                $http({
                    url: './taiga/course_tasks',
                    method: "GET",
                    headers: {
                        'course': $scope.courseid,
                        'weekBeginning': $scope.rawWeekBeginning,
                        'weekEnding': $scope.rawWeekEnding
                    }
                }).then(function (response) {
                    console.log("Worked!");
                    console.log(response.data);
                    $scope.courseTasks = response.data;
                });
            }
        }

    }])

    .controller('TeamController', ['$scope', '$location', '$routeParams', 'courseService', 'teamService', 'userService', '$http', function ($scope, $location, $routeParams, courseService, teamService, userService, $http) {
        $scope.teamid = $routeParams.team_id;
        var course = courseService.getCourse();
        $scope.courseid = courseService.getCourse();
        if (course == null) {
            course = "none";
        }
        console.log("course: " + course);

        if ($scope.teamid == null) {
            $scope.teamid = "none";
        }

        console.log("team: " + $scope.teamid);

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
            console.log("Worked!");
            console.log(response.data);
            if (response.data == false) {
                $location.path('/home');
            } else {
                getCourseWeightFreq();
            }
        });

        function getCourseWeightFreq() {
            $http({
                url: './taiga/course_weightFreq',
                method: "GET",
                headers: {'course': course}
            }).then(function (response) {
                console.log("Worked!");
                console.log(response.data);
                $scope.courseWeightFreq = response.data;
                getTeamWeightFreq();
            });
        }

        function getTeamWeightFreq() {
            $http({
                url: './taiga/team_weightFreq',
                method: "GET",
                headers: {'course': course, 'team': $scope.teamid}
            }).then(function (response) {
                console.log("Worked!");
                console.log(response.data);
                $scope.weightFreq = response.data;
                getTaigaActivity();
            });
        }

        function getTaigaActivity() {
            $http({
                url: './taiga/team_activity',
                method: "GET",
                headers: {'course': course, 'team': $scope.teamid}
            }).then(function (response) {
                console.log("Worked!");
                console.log(response.data);
                $scope.teamActivity = response.data;
                getTaigaIntervals();
            });
        }

        function getTaigaIntervals() {
            $http({
                url: './taiga/team_intervals',
                method: "GET",
                headers: {'course': course, 'team': $scope.teamid}
            }).then(function (response) {
                console.log("Worked!");
                console.log(response.data);
                $scope.teamIntervals = response.data;
            });
        }

        $scope.IntervalChangedBegin = function (rawWeekBeginning) {
            $scope.rawWeekBeginning = rawWeekBeginning;
            console.log("WeekBeginning: " + $scope.rawWeekBeginning);
            if ($scope.rawWeekEnding != null) {
                $http({
                    url: './taiga/team_tasks',
                    method: "GET",
                    headers: {
                        'course': course,
                        'team': $scope.teamid,
                        'weekBeginning': $scope.rawWeekBeginning,
                        'weekEnding': $scope.rawWeekEnding
                    }
                }).then(function (response) {
                    console.log("Worked!");
                    console.log(response.data);
                    $scope.teamTasks = response.data;
                });
            }
        }

        $scope.IntervalChangedEnd = function (rawWeekEnding) {
            $scope.rawWeekEnding = rawWeekEnding;
            console.log("WeekEnding: " + $scope.rawWeekEnding);
            if ($scope.rawWeekBeginning != null) {
                $http({
                    url: './taiga/team_tasks',
                    method: "GET",
                    headers: {
                        'course': course,
                        'team': $scope.teamid,
                        'weekBeginning': $scope.rawWeekBeginning,
                        'weekEnding': $scope.rawWeekEnding
                    }
                }).then(function (response) {
                    console.log("Worked!");
                    console.log(response.data);
                    $scope.teamTasks = response.data;
                });
            }
        }

    }])

    .controller('StudentController', ['$filter', '$scope', '$location', '$routeParams', 'courseService', 'teamService', 'studentService', 'userService', '$http', function ($filter, $scope, $location, $routeParams, courseService, teamService, studentService, userService, $http) {
        $scope.studentid = $routeParams.student_id;
        $scope.courseid = courseService.getCourse();
        $scope.teamid = teamService.getTeam();

        var course = courseService.getCourse();
        var team = teamService.getTeam();
        var studentemail = studentService.getStudentEmail();

        if (course == null) {
            course = "none";
            console.log("course: " + course);
        }

        if (team == null) {
            team = "none";
            console.log("team: " + team);
        }

        if (studentemail == null) {
            studentemail = "none";
            console.log("studentemail: " + studentemail);
        }

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
            console.log("Worked!");
            console.log(response.data);
            if (response.data == false) {
                $location.path('/home');
            } else {
                getCourseWeightFreq();
            }
        });

        function getCourseWeightFreq() {
            $http({
                url: './taiga/course_weightFreq',
                method: "GET",
                headers: {'course': course}
            }).then(function (response) {
                console.log("Worked!");
                console.log(response.data);
                $scope.courseWeightFreq = response.data;
                getTeamWeightFreq();
            });
        }

        function getTeamWeightFreq() {
            $http({
                url: './taiga/team_weightFreq',
                method: "GET",
                headers: {'course': course, 'team': team}
            }).then(function (response) {
                console.log("Worked!");
                console.log(response.data);
                $scope.teamWeightFreq = response.data;
                getStudentWeightFreq();
            });
        }

        function getStudentWeightFreq() {
            $http({
                url: './taiga/student_weightFreq',
                method: "GET",
                headers: {'course': course, 'team': team, 'email': studentemail}
            }).then(function (response) {
                console.log("Worked!");
                console.log(response.data);
                $scope.weightFreq = response.data;
                getTaigaActivity();
            });
        }

        function getTaigaActivity() {
            $http({
                url: './taiga/student_activity',
                method: "GET",
                headers: {'course': course, 'team': team, 'email': studentemail}
            }).then(function (response) {
                console.log("Worked!");
                console.log(response.data);
                $scope.studentActivity = response.data;
                getTaigaIntervals();
            });
        }

        function getTaigaIntervals() {
            $http({
                url: './taiga/student_intervals',
                method: "GET",
                headers: {'course': course, 'team': team, 'email': studentemail}
            }).then(function (response) {
                console.log("Worked!");
                console.log(response.data);
                $scope.studentIntervals = response.data;
            });
        }

        $scope.IntervalChangedBegin = function (rawWeekBeginning) {
            $scope.rawWeekBeginning = rawWeekBeginning;
            console.log("WeekBeginning: " + $scope.rawWeekBeginning);
            if ($scope.rawWeekEnding != null) {
                $http({
                    url: './taiga/student_tasks',
                    method: "GET",
                    headers: {
                        'course': course,
                        'team': team,
                        'email': studentemail,
                        'weekBeginning': $scope.rawWeekBeginning,
                        'weekEnding': $scope.rawWeekEnding
                    }
                }).then(function (response) {
                    console.log("Worked!");
                    console.log(response.data);
                    $scope.studentTasks = response.data;
                });
            }
        }

        $scope.IntervalChangedEnd = function (rawWeekEnding) {
            $scope.rawWeekEnding = rawWeekEnding;
            console.log("WeekEnding: " + $scope.rawWeekEnding);
            if ($scope.rawWeekBeginning != null) {
                $http({
                    url: './taiga/student_tasks',
                    method: "GET",
                    headers: {
                        'course': course,
                        'team': team,
                        'email': studentemail,
                        'weekBeginning': $scope.rawWeekBeginning,
                        'weekEnding': $scope.rawWeekEnding
                    }
                }).then(function (response) {
                    console.log("Worked!");
                    console.log(response.data);
                    $scope.studentTasks = response.data;
                });
            }
        }

    }])
    .controller("TaigaAdmin", ['$scope', '$http', function ($scope, $http) {

        $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";

        $scope.updateTaigaProjects = function () {
            $http({
                url: './taiga/Update/Projects',
                method: "POST"
            }).then(function (response) {
                console.log("Worked!");
                //console.log(response.data);
                $scope.tasks = response.data;
            }, function (response) {
                //fail case
                console.log("didn't work");
                //console.log(response);
                $scope.message = response;
            });

        };

        $scope.updateTaigaMemberships = function () {
            $http({
                url: './taiga/Update/Memberships',
                method: "POST"
            }).then(function (response) {
                console.log("Worked!");
                //console.log(response.data);
                $scope.tasks = response.data;
            }, function (response) {
                //fail case
                console.log("didn't work");
                //console.log(response);
                $scope.message = response;
            });

        };

        $scope.updateTaigaTaskTotals = function () {
            $http({
                url: './taiga/Update/Tasks',
                method: "POST"
            }).then(function (response) {
                console.log("Worked!");
                //console.log(response.data);
                $scope.tasks = response.data;
            }, function (response) {
                //fail case
                console.log("didn't work");
                //console.log(response);
                $scope.message = response;
            });

        };

    }]);
