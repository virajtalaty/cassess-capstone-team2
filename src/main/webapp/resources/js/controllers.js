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
        };

        $scope.selectedTeamChanged = function () {
            //console.log(response.data);
            $scope.team = $scope.studentTeam.value.team;
            console.log($scope.team);
        };

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
        };

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
    .controller('NavController', ['$scope', '$location', '$http', 'courseService', 'teamService', 'studentService', '$window', '$routeParams', 'userService',
        function ($scope, $location, $http, courseService, teamService, studentService, $window, $routeParams, userService) {

            $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";

            function getCourses(user) {
                if (userService.getAuth() == 'super_user') {
                    $http({
                        url: './taigaCourses',
                        method: "GET"
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
            };

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
                getTaigaWeightFreq();
            }
        });

        function getTaigaWeightFreq() {
            $http({
                url: './taiga/course_quickweightFreq',
                method: "GET",
                headers: {'course': $scope.courseid}
            }).then(function (response) {
                $scope.courseArrayTG = response.data;
                getGitHubWeightFreq();
            });
        }

        function getGitHubWeightFreq() {
            $http({
                url: './github/course_quickweightFreq',
                method: "GET",
                headers: {'course': $scope.courseid}
            }).then(function (response) {
                $scope.courseArrayGH = response.data;
                plotCurrentWeek();
            });
        }

        function plotCurrentWeek() {
            var trace1 = {
                r: [$scope.courseArrayTG[0].weight, $scope.courseArrayTG[0].frequency, $scope.courseArrayGH[0].weight, $scope.courseArrayGH[0].frequency],
                t: ['TG Impact', 'TG Freq', 'GH Impact', 'GH Freq'],
                mode: 'lines+markers',
                name: 'Course',
                marker: {size:16},
                line: {width: 50},
                color: '#810',
                width: 20,
                type: 'scatter',
                hoverinfo: ["r"],
                connectgaps: true
            };

            var data = [trace1];

            var layout = {
                title: $scope.courseArrayTG[0].weekBeginning + ' to ' + $scope.courseArrayTG[0].weekEnding,
                showlegend: true,
                width: 600,
                height: 400,
                margin: {
                    l: 40,
                    r: 250,
                    b: 20,
                    t: 40,
                    pad: 0
                },
                legend: {
                    r: 4,
                    t: 340,
                    font: {
                        family: 'sans-serif',
                        size: 10,
                        color: '#000'
                    }
                },
                font: {
                    family: 'Arial',
                    size: 16,
                    color: '#066'
                },
                angularaxis: {
                    tickorientation:'vertical'
                },
                radialaxis: {
                    range:[0,3],
                    tickfont: {
                        size: 1,
                    }
                },
                paper_bgcolor: 'rgba(0,0,0,0)',
                plot_bgcolor: 'rgba(0,0,0,0)'

            };

            Plotly.newPlot('currentWeek', data, layout);
            plotPreviousWeek();
        }

        function plotPreviousWeek() {

            var trace1 = {
                r: [$scope.courseArrayTG[1].weight, $scope.courseArrayTG[1].frequency, $scope.courseArrayGH[1].weight, $scope.courseArrayGH[1].frequency],
                t: ['TG Impact', 'TG Freq', 'GH Impact', 'GH Freq'],
                mode: 'lines+markers',
                name: 'Course',
                marker: {size:16},
                line: {width: 50},
                color: '#810',
                width: 20,
                type: 'scatter',
                hoverinfo: ["r"],
                connectgaps: true
            };


            var data = [trace1];

            var layout = {
                title: $scope.courseArrayTG[1].weekBeginning + ' to ' + $scope.courseArrayTG[1].weekEnding,
                showlegend: true,
                width: 600,
                height: 400,
                margin: {
                    l: 40,
                    r: 250,
                    b: 20,
                    t: 40,
                    pad: 0
                },
                legend: {
                    r: 4,
                    t: 340,
                    font: {
                        family: 'sans-serif',
                        size: 10,
                        color: '#000'
                    }
                },
                font: {
                    family: 'Arial',
                    size: 16,
                    color: '#066'
                },
                angularaxis: {
                    tickorientation:'vertical'
                },
                radialaxis: {
                    range:[0,3],
                    tickfont: {
                        size: 1,
                    }
                },
                paper_bgcolor: 'rgba(0,0,0,0)',
                plot_bgcolor: 'rgba(0,0,0,0)'
            };

            Plotly.newPlot('previousWeek', data, layout);
            getTaigaActivity();
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
                $scope.dataForTaigaCourseActivity =  getDataForCourseTaigaActivityCharts(response.data);
                getTaigaIntervals();
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
                    axisLabel: 'Week Of',
                    tickFormat: function(d) {
                        return d3.time.format('%m/%d/%y')(new Date(d))
                    },

                    showMaxMin: false,
                    staggerLabels: false
                },

                yAxis: {
                    axisLabel: 'Taiga Task Activity',
                    axisLabelDistance: 0,
                    tickValues:  [0, 5, 10, 15, 20, 25, 30]
                },

                yDomain:[0, 30]

            }
        };

        function getDataForCourseTaigaActivityCharts(array){

            var data = []; var inProgress = []; var toTest = []; var done = [];var expected = [];

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
            }

            data.push({color: "#6799ee", key: "IN PROGRESS", values: inProgress, strokeWidth: 2});
            data.push({color: "#000000", key: "READY FOR TEST", values: toTest, strokeWidth: 2});
            data.push({color: "#2E8B57", key: "CLOSED", values: done, strokeWidth: 2});
            data.push({color: "#8f65b6", key: "EXPECTED", values: expected, classed: "dashed"});

            return data;
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
                    console.log("Worked, these are the averages for the week for weekBegin");
                    console.log(response.data);
                    $scope.courseTasks = response.data;
                    $scope.dataForTaigaCourseTasks = getDataForTaigaCourseTasks(response.data);
                });
            }
        };

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
                    console.log("Worked, these are the averages for the week for weekEnd!");
                    console.log(response.data);
                    $scope.courseTasks = response.data;
                    $scope.dataForTaigaCourseTasks = getDataForTaigaCourseTasks(response.data);
                });
            }
        };

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

                x: function(d){ return d[0]; },
                y: function(d){ return d[1]; },

                clipEdge: true,
                duration: 500,
                stacked: false,

                xAxis: {
                    axisLabel: 'Days',
                    showMaxMin: false
                },

                yAxis: {
                    axisLabel: 'Taiga Task Totals',
                    axisLabelDistance: -10
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

        ////* Function to Parse GitHub CommitData for MultiBar Chart * ////

        function getDataForGitHubCourseCommitsCharts(array){

            var commits = []; var linesOfCodeAdded = []; var linesOfCodeDeleted = []; var data = [];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];var valueset2 = [];var valueset3 = [];

                valueset1.push(array[i].GitHubDataPK.date);
                valueset1.push(array[i].commits);

                valueset2.push(array[i].GitHubDataPK.date);
                valueset2.push(array[i].linesOfCodeAdded/1000);

                valueset3.push(array[i].GitHubDataPK.date);
                valueset3.push(array[i].linesOfCodeDeleted/100);

                commits.push(valueset1);
                linesOfCodeAdded.push(valueset2);
                linesOfCodeDeleted.push(valueset3);
            }

            data.push({color: "#6799ee", key: "Commits", values: commits});
            data.push({color: "#000000", key: "Lines Of Code Added", values: linesOfCodeAdded});
            data.push({color: "#2E8B57", key: "Lines Of Code Deleted", values: linesOfCodeDeleted});

            return data;
        }

        ////* Function to Parse GitHub Weight for Line Chart * ////

        function getDataForGitHubCourseWeightCharts(array){

            var weight = []; var expected = []; var data = [];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];var valueset2 = [];

                valueset1.push(Date.parse(array[i].GitHubDataPK.date));
                valueset1.push(array[i].weight);

                valueset2.push(Date.parse(array[i].GitHubDataPK.date));
                valueset2.push(2);

                weight.push(valueset1);
                expected.push(valueset2);
            }

            data.push({color: "#6799ee", key: "Weight", values: weight, strokeWidth: 2});
            data.push({color: "#000000", key: "Expected", values: expected, strokeWidth: 2});

            return data;
        }



        var fireRefreshEventOnWindow = function () {
            var evt = document.createEvent("HTMLEvents");
            evt.initEvent('resize', true, false);
            window.dispatchEvent(evt);
        };

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
                getTaigaCourseWeightFreq();
            }
        });

        function getTaigaCourseWeightFreq() {
            $http({
                url: './taiga/course_quickweightFreq',
                method: "GET",
                headers: {'course': course}
            }).then(function (response) {
                $scope.courseArrayTG = response.data;
                getGitHubCourseWeightFreq();
            });
        }

        function getGitHubCourseWeightFreq() {
            $http({
                url: './github/course_quickweightFreq',
                method: "GET",
                headers: {'course': course}
            }).then(function (response) {
                $scope.courseArrayGH = response.data;
                getTaigaTeamWeightFreq();
            });
        }

        function getTaigaTeamWeightFreq() {
            $http({
                url: './taiga/team_quickweightFreq',
                method: "GET",
                headers: {'course': course, 'team': $scope.teamid}
            }).then(function (response) {
                $scope.teamArrayTG = response.data;
                getGitHubTeamWeightFreq();
            });
        }

        function getGitHubTeamWeightFreq() {
            $http({
                url: './github/team_quickweightFreq',
                method: "GET",
                headers: {'course': course, 'team': $scope.teamid}
            }).then(function (response) {
                $scope.teamArrayGH = response.data;
                plotCurrentWeek();
            });
        }

        function plotCurrentWeek() {
            var trace1 = {
                r: [$scope.courseArrayTG[0].weight, $scope.courseArrayTG[0].frequency, $scope.courseArrayGH[0].weight, $scope.courseArrayGH[0].frequency],
                t: ['TG Impact', 'TG Freq', 'GH Impact', 'GH Freq'],
                mode: 'lines+markers',
                name: 'Course',
                marker: {size:16},
                line: {width: 50},
                color: '#810',
                width: 20,
                type: 'scatter',
                hoverinfo: ["r"],
                showlegend: true,
                connectgaps: true
            };

            var trace2 = {
                r: [$scope.teamArrayTG[0].weight*.75, $scope.teamArrayTG[0].frequency*.75, $scope.teamArrayGH[0].weight*.75, $scope.teamArrayGH[0].frequency*.75],
                t: ['TG Impact', 'TG Freq', 'GH Impact', 'GH Freq'],
                mode: 'lines+markers',
                name: 'Team',
                marker: {size:16},
                line: {width: 50},
                color: '#180',
                width: 20,
                type: 'scatter',
                hoverinfo: ["r"],
                connectgaps: true
            };

            var data = [trace1, trace2];

            var layout = {
                title: $scope.teamArrayTG[0].weekBeginning + ' to ' + $scope.teamArrayTG[0].weekEnding,
                showlegend: true,
                width: 600,
                height: 400,
                margin: {
                    l: 40,
                    r: 250,
                    b: 20,
                    t: 40,
                    pad: 0
                },
                legend: {
                    r: 4,
                    t: 340,
                    font: {
                        family: 'sans-serif',
                        size: 10,
                        color: '#000'
                    }
                },
                font: {
                    family: 'Arial',
                    size: 16,
                    color: '#066'
                },
                angularaxis: {
                    tickorientation:'vertical'
                },
                radialaxis: {
                    range:[0,3],
                    tickfont: {
                        size: 1,
                    }
                },
                paper_bgcolor: 'rgba(0,0,0,0)',
                plot_bgcolor: 'rgba(0,0,0,0)'

            };

            Plotly.newPlot('currentWeek', data, layout);
            plotPreviousWeek();
        }

        function plotPreviousWeek() {

            var trace1 = {
                r: [$scope.courseArrayTG[1].weight, $scope.courseArrayTG[1].frequency, $scope.courseArrayGH[1].weight, $scope.courseArrayGH[1].frequency],
                t: ['TG Impact', 'TG Freq', 'GH Impact', 'GH Freq'],
                mode: 'lines+markers',
                name: 'Course',
                marker: {size:16},
                line: {width: 50},
                color: '#810',
                width: 20,
                type: 'scatter',
                hoverinfo: ["r"],
                connectgaps: true
            };

            var trace2 = {
                r: [$scope.teamArrayTG[1].weight*.75, $scope.teamArrayTG[1].frequency*.75, $scope.teamArrayGH[1].weight*.75, $scope.teamArrayGH[1].frequency*.75],
                t: ['TG Impact', 'TG Freq', 'GH Impact', 'GH Freq'],
                mode: 'lines+markers',
                name: 'Team',
                marker: {size:16},
                line: {width: 50},
                color: '#180',
                width: 20,
                type: 'scatter',
                hoverinfo: ["r"],
                connectgaps: true
            };


            var data = [trace1, trace2];

            var layout = {
                title: $scope.teamArrayTG[1].weekBeginning + ' to ' + $scope.teamArrayTG[1].weekEnding,
                showlegend: true,
                width: 600,
                height: 400,
                margin: {
                    l: 40,
                    r: 250,
                    b: 20,
                    t: 40,
                    pad: 0
                },
                legend: {
                    r: 4,
                    t: 340,
                    font: {
                        family: 'sans-serif',
                        size: 10,
                        color: '#000'
                    }
                },
                font: {
                    family: 'Arial',
                    size: 16,
                    color: '#066'
                },
                angularaxis: {
                    tickorientation:'vertical'
                },
                radialaxis: {
                    range:[0,3],
                    tickfont: {
                        size: 1,
                    }
                },
                paper_bgcolor: 'rgba(0,0,0,0)',
                plot_bgcolor: 'rgba(0,0,0,0)'
            };

            Plotly.newPlot('previousWeek', data, layout);
            getTaigaActivity();
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
                $scope.dataForTaigaTeamActivity = getDataForTeamTaigaActivityCharts(response.data);
                getTaigaIntervals();
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
                    axisLabel: 'Week Of',
                    tickFormat: function(d) {
                        return d3.time.format('%m/%d/%y')(new Date(d))
                    },

                    showMaxMin: false,
                    staggerLabels: false
                },

                yAxis: {
                    axisLabel: 'Taiga Task Activity',
                    axisLabelDistance: 0,
                    tickValues:  [0, 5, 10, 15, 20, 25, 30]
                },

                yDomain:[0, 30]

            }
        };

        function getDataForTeamTaigaActivityCharts(array){

            var data = []; var inProgress = []; var toTest = []; var done = [];var expected = [];

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
            }

            data.push({color: "#6799ee", key: "IN PROGRESS", values: inProgress, strokeWidth: 2});
            data.push({color: "#000000", key: "READY FOR TEST", values: toTest, strokeWidth: 2});
            data.push({color: "#2E8B57", key: "CLOSED", values: done, strokeWidth: 2});
            data.push({color: "#8f65b6", key: "EXPECTED", values: expected, classed: "dashed"});

            return data;
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
                    $scope.dataForTaigaTeamTasks = getDataForTaigaTeamTasks(response.data);
                });
            }
        };

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
                    $scope.dataForTaigaTeamTasks = getDataForTaigaTeamTasks(response.data);
                });
            }
        };

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
                    axisLabel: 'Days',
                    showMaxMin: false
                },

                yAxis: {
                    axisLabel: 'Taiga Task Totals',
                    axisLabelDistance: -10
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

        ////* Function to Parse GitHub CommitData for MultiBar Chart * ////

        function getDataForGitHubTeamCommitsCharts(array){

            var commits = []; var linesOfCodeAdded = []; var linesOfCodeDeleted = []; var data = [];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];var valueset2 = [];var valueset3 = [];

                valueset1.push(array[i].GitHubDataPK.date);
                valueset1.push(array[i].commits);

                valueset2.push(array[i].GitHubDataPK.date);
                valueset2.push(array[i].linesOfCodeAdded/1000);

                valueset3.push(array[i].GitHubDataPK.date);
                valueset3.push(array[i].linesOfCodeDeleted/100);

                commits.push(valueset1);
                linesOfCodeAdded.push(valueset2);
                linesOfCodeDeleted.push(valueset3);
            }

            data.push({color: "#6799ee", key: "Commits", values: commits});
            data.push({color: "#000000", key: "Lines Of Code Added", values: linesOfCodeAdded});
            data.push({color: "#2E8B57", key: "Lines Of Code Deleted", values: linesOfCodeDeleted});

            return data;
        }

        ////* Function to Parse GitHub Weight for Line Chart * ////

        function getDataForGitHubTeamWeightCharts(array){

            var weight = []; var expected = []; var data = [];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];var valueset2 = [];

                valueset1.push(Date.parse(array[i].GitHubDataPK.date));
                valueset1.push(array[i].weight);

                valueset2.push(Date.parse(array[i].GitHubDataPK.date));
                valueset2.push(2);

                weight.push(valueset1);
                expected.push(valueset2);
            }

            data.push({color: "#6799ee", key: "Weight", values: weight, strokeWidth: 2});
            data.push({color: "#000000", key: "Expected", values: expected, strokeWidth: 2});

            return data;
        }

        var fireRefreshEventOnWindow = function () {
            var evt = document.createEvent("HTMLEvents");
            evt.initEvent('resize', true, false);
            window.dispatchEvent(evt);
        };

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
                getTaigaCourseWeightFreq();
            }
        });

        function getTaigaCourseWeightFreq() {
            $http({
                url: './taiga/course_quickweightFreq',
                method: "GET",
                headers: {'course': course}
            }).then(function (response) {
                $scope.courseArrayTG = response.data;
                getGitHubCourseWeightFreq();
            });
        }

        function getGitHubCourseWeightFreq() {
            $http({
                url: './github/course_quickweightFreq',
                method: "GET",
                headers: {'course': course}
            }).then(function (response) {
                $scope.courseArrayGH = response.data;
                getTaigaTeamWeightFreq();
            });
        }

        function getTaigaTeamWeightFreq() {
            $http({
                url: './taiga/team_quickweightFreq',
                method: "GET",
                headers: {'course': course, 'team': team}
            }).then(function (response) {
                $scope.teamArrayTG = response.data;
                getGitHubTeamWeightFreq();
            });
        }

        function getGitHubTeamWeightFreq() {
            $http({
                url: './github/team_quickweightFreq',
                method: "GET",
                headers: {'course': course, 'team': team}
            }).then(function (response) {
                $scope.teamArrayGH = response.data;
                getTaigaStudentWeightFreq();
            });
        }

        function getTaigaStudentWeightFreq() {
            $http({
                url: './taiga/student_quickweightFreq',
                method: "GET",
                headers: {'course': course, 'team': team, 'email': studentemail}
            }).then(function (response) {
                $scope.studentArrayTG = response.data;
                getGitHubStudentWeightFreq();
            });
        }

        function getGitHubStudentWeightFreq() {
            $http({
                url: './github/student_quickweightFreq',
                method: "GET",
                headers: {'course': course, 'team': team, 'email': studentemail}
            }).then(function (response) {
                $scope.studentArrayGH = response.data;
                plotCurrentWeek();
            });
        }

        function plotCurrentWeek() {
            var trace1 = {
                r: [$scope.courseArrayTG[0].weight, $scope.courseArrayTG[0].frequency, $scope.courseArrayGH[0].weight, $scope.courseArrayGH[0].frequency],
                t: ['TG Impact', 'TG Freq', 'GH Impact', 'GH Freq'],
                mode: 'lines+markers',
                name: 'Course',
                marker: {size:16},
                line: {width: 50},
                color: '#810',
                width: 20,
                type: 'scatter',
                hoverinfo: ["r"],
                connectgaps: true
            };

            var trace2 = {
                r: [$scope.teamArrayTG[0].weight*.75, $scope.teamArrayTG[0].frequency*.75, $scope.teamArrayGH[0].weight*.75, $scope.teamArrayGH[0].frequency*.75],
                t: ['TG Impact', 'TG Freq', 'GH Impact', 'GH Freq'],
                mode: 'lines+markers',
                name: 'Team',
                marker: {size:16},
                line: {width: 50},
                color: '#180',
                width: 20,
                type: 'scatter',
                hoverinfo: ["r"],
                connectgaps: true
            };

            var trace3 = {
                r: [$scope.studentArrayTG[0].weight, $scope.studentArrayTG[0].frequency, $scope.studentArrayGH[0].weight, $scope.studentArrayGH[0].frequency],
                t: ['TG Impact', 'TG Freq', 'GH Impact', 'GH Freq'],
                mode: 'lines+markers',
                name: 'Student',
                marker: {size:16},
                line: {width: 50},
                color: '#118',
                width: 2,
                type: 'scatter',
                hoverinfo: ["r"],
                connectgaps: true
            };

            var data = [trace1, trace2, trace3];

            var layout = {
                title: $scope.studentArrayTG[0].weekBeginning + ' to ' + $scope.studentArrayTG[0].weekEnding,
                showlegend: true,
                width: 600,
                height: 400,
                margin: {
                    l: 40,
                    r: 250,
                    b: 20,
                    t: 40,
                    pad: 0
                },
                legend: {
                    r: 4,
                    t: 340,
                    font: {
                        family: 'sans-serif',
                        size: 10,
                        color: '#000'
                    }
                },
                font: {
                    family: 'Arial',
                    size: 16,
                    color: '#066'
                },
                angularaxis: {
                    tickorientation:'vertical'
                },
                radialaxis: {
                    range:[0,3],
                    tickfont: {
                        size: 1
                    }
                },
                paper_bgcolor: 'rgba(0,0,0,0)',
                plot_bgcolor: 'rgba(0,0,0,0)'

            };

            Plotly.newPlot('currentWeek', data, layout);
            plotPreviousWeek();
        }

        function plotPreviousWeek() {

            var trace1 = {
                r: [$scope.courseArrayTG[1].weight, $scope.courseArrayTG[1].frequency, $scope.courseArrayGH[1].weight, $scope.courseArrayGH[1].frequency],
                t: ['TG Impact', 'TG Freq', 'GH Impact', 'GH Freq'],
                mode: 'lines+markers',
                name: 'Course',
                marker: {size:16},
                line: {width: 50},
                color: '#810',
                width: 20,
                type: 'scatter',
                hoverinfo: ["r"],
                connectgaps: true
            };

            var trace2 = {
                r: [$scope.teamArrayTG[1].weight*.75, $scope.teamArrayTG[1].frequency*.75, $scope.teamArrayGH[1].weight*.75, $scope.teamArrayGH[1].frequency*.75],
                t: ['TG Impact', 'TG Freq', 'GH Impact', 'GH Freq'],
                mode: 'lines+markers',
                name: 'Team',
                marker: {size:16},
                line: {width: 50},
                color: '#180',
                width: 20,
                type: 'scatter',
                hoverinfo: ["r"],
                connectgaps: true
            };

            var trace3 = {
                r: [$scope.studentArrayTG[1].weight, $scope.studentArrayTG[1].frequency, $scope.studentArrayGH[1].weight, $scope.studentArrayGH[1].frequency],
                t: ['TG Impact', 'TG Freq', 'GH Impact', 'GH Freq'],
                mode: 'lines+markers',
                name: 'Student',
                marker: {size:16},
                line: {width: 50},
                color: '#118',
                width: 20,
                type: 'scatter',
                hoverinfo: ["r"],
                connectgaps: true
            };

            var data = [trace1, trace2, trace3];
            var layout = {
                title: $scope.studentArrayTG[1].weekBeginning + ' to ' + $scope.studentArrayTG[1].weekEnding,
                showlegend: true,
                width: 600,
                height: 400,
                margin: {
                    l: 40,
                    r: 250,
                    b: 20,
                    t: 40,
                    pad: 0
                },
                legend: {
                    r: 4,
                    t: 340,
                    font: {
                        family: 'sans-serif',
                        size: 10,
                        color: '#000'
                    }
                },
                font: {
                    family: 'Arial',
                    size: 16,
                    color: '#066'
                },
                angularaxis: {
                    tickorientation:'vertical'
                },
                radialaxis: {
                    range:[0,3],
                    tickfont: {
                        size: 1
                    }
                },
                paper_bgcolor: 'rgba(0,0,0,0)',
                plot_bgcolor: 'rgba(0,0,0,0)'
            };

            Plotly.newPlot('previousWeek', data, layout);
            getTaigaActivity();
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
                $scope.dataForTaigaStudentActivity =  getDataForStudentTaigaActivityCharts(response.data);
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
                    axisLabel: 'Week Of',
                    tickFormat: function(d) {
                        return d3.time.format('%m/%d/%y')(new Date(d))
                    },

                    showMaxMin: false,
                    staggerLabels: false
                },

                yAxis: {
                    axisLabel: 'Taiga Task Activity',
                    axisLabelDistance: 0,
                    tickValues:  [0, 5, 10, 15, 20, 25, 30]
                },

                yDomain:[0, 30]

            }
        };

        function getDataForStudentTaigaActivityCharts(array){

            var data = []; var inProgress = []; var toTest = []; var done = [];var expected = [];

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
            }

            data.push({color: "#6799ee", key: "IN PROGRESS", values: inProgress, strokeWidth: 2});
            data.push({color: "#000000", key: "READY FOR TEST", values: toTest, strokeWidth: 2});
            data.push({color: "#2E8B57", key: "CLOSED", values: done, strokeWidth: 2});
            data.push({color: "#8f65b6", key: "EXPECTED", values: expected, classed: "dashed"});

            return data;
        }

        function getTaigaIntervals() {
            $http({
                url: './taiga/student_intervals',
                method: "GET",
                headers: {'course': course, 'team': team, 'email': studentemail}
            }).then(function (response) {
                console.log("Worked This shows the intervals!");
                console.log(response.data);
                $scope.studentIntervals = response.data;
                getGitHubCommitsData();
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
                    console.log("Worked! Begin Changed: ");
                    console.log(response.data);

                    $scope.studentTasks = response.data;
                    $scope.dataForTaigaStudentTasks = getDataForTaigaStudentTasks(response.data);
                });
            }
        };

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
                    $scope.dataForTaigaStudentTasks = getDataForTaigaStudentTasks(response.data);
                });
            }
        };

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
                    axisLabel: 'Days',
                    showMaxMin: false
                },

                yAxis: {
                    axisLabel: 'Taiga Task Totals',
                    axisLabelDistance: -10
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

        function getGitHubCommitsData() {
            $http({
                url: './github/commits',
                method: "GET",
                headers: {'email': studentemail}
            }).then(function (response) {
                console.log("Worked This is what the GitHub Data is showing: !");
                console.log(response.data);
                $scope.dataForGitHubStudentCommits =  getDataForGitHubStudentCommitsCharts(response.data);
                getGitHubWeightData();
            });
        }

        $scope.optionsForGitHubStudentCommits = {

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
                    axisLabel: 'Days',
                    showMaxMin: false
                },

                yAxis: {
                    axisLabel: 'GitHub Commit Counts',
                    axisLabelDistance: -10
                }
            }
        };

        ////* Function to Parse GitHub CommitData for MultiBar Chart * ////

        function getDataForGitHubStudentCommitsCharts(array){

            var commits = []; var linesOfCodeAdded = []; var linesOfCodeDeleted = []; var data = [];

            for (var i = 0; i < array.length; i++){

                var valueset1 = [];var valueset2 = [];var valueset3 = [];

                valueset1.push(array[i].GitHubDataPK.date);
                valueset1.push(array[i].commits);

                valueset2.push(array[i].GitHubDataPK.date);
                valueset2.push(array[i].linesOfCodeAdded/1000);

                valueset3.push(array[i].GitHubDataPK.date);
                valueset3.push(array[i].linesOfCodeDeleted/100);

                commits.push(valueset1);
                linesOfCodeAdded.push(valueset2);
                linesOfCodeDeleted.push(valueset3);
            }

            data.push({color: "#6799ee", key: "Commits", values: commits});
            data.push({color: "#000000", key: "Lines Of Code Added/1000", values: linesOfCodeAdded});
            data.push({color: "#2E8B57", key: "Lines Of Code Deleted/100", values: linesOfCodeDeleted});

            return data;
        }

        function getGitHubWeightData() {
            $http({
                url: './github/weight',
                method: "GET",
                headers: {'email': studentemail}
            }).then(function (response) {
                console.log("Worked This is what the GitHub Weight is showing: !");
                console.log(response.data);
                $scope.dataForGitHubStudentWeight= getDataForGitHubStudentWeightCharts(response.data);
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
                    axisLabel: 'Week Ending On',
                    tickFormat: function(d) {
                        return d3.time.format('%m/%d/%y')(new Date(d))
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

                valueset1.push(Date.parse(array[i].GitHubDataPK.date));
                valueset1.push(array[i].weight);

                valueset2.push(Date.parse(array[i].GitHubDataPK.date));
                valueset2.push(2);

                weight.push(valueset1);
                expected.push(valueset2);
            }

            data.push({color: "#6799ee", key: "Weight", values: weight, strokeWidth: 2});
            data.push({color: "#000000", key: "Expected", values: expected, strokeWidth: 2});

            return data;
        }


        var fireRefreshEventOnWindow = function () {
            var evt = document.createEvent("HTMLEvents");
            evt.initEvent('resize', true, false);
            window.dispatchEvent(evt);
        };



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
