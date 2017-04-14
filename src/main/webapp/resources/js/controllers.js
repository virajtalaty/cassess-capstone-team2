'use strict';

myapp.service('userService', function() {
    var user = null;
    var auth = null;

    var setUser = function(userObject){
        user = userObject;
    }

    var getUser = function(){
        return user;
    }

    var setAuth = function(authObject){
        auth = authObject;
    }

    var getAuth = function(){
        return auth;
    }
    return {
        setUser: setUser,
        getUser: getUser,
        setAuth: setAuth,
        getAuth: getAuth
    };
})
    .service('courseService', function() {
    var course = null;

    var setCourse = function(courseObject){
        course = courseObject;
    }

    var getCourse = function(){
        return course;
    }
    return {
        setCourse: setCourse,
        getCourse: getCourse
    };
})
    .service('teamService', function() {
        var team = null;

        var setTeam = function(teamName){
            team = teamName;
        }

        var getTeam = function(){
            return team;
        }
        return {
            setTeam: setTeam,
            getTeam: getTeam
        };
    })
    .service('studentService', function() {
        var studentEmail = null;
        var studentName = null;

        var setStudentEmail = function(email){
            studentEmail = email;
        }

        var getStudentEmail = function(){
            return studentEmail;
        }

        var setStudentName = function(studentname){
            studentName = studentname;
        }

        var getStudentName = function(){
            return studentName;
        }
        return {
            setStudentEmail: setStudentEmail,
            getStudentEmail: getStudentEmail,
            setStudentName: setStudentName,
            getStudentName: getStudentName
        };
    });

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
    .controller('UsersController', function ($scope, $log, UsersService) {
        $scope.users = UsersService.getAll();
    })
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

    }).controller('NavController', ['$scope', '$location', '$http', 'courseService', 'teamService', 'studentService', '$window', '$routeParams', 'userService',
    function($scope, $location, $http, courseService, teamService, studentService, $window, $routeParams, userService) {



        $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";

        function getCourses(user) {
            if(userService.getAuth() == 'admin'){
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
            if(userService.getAuth() == 'user'){
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
            url : './current_user',
            method : "GET"
        }).then(function(response) {
            console.log("Worked!");
            console.log(response.data);
            $scope.user = response.data;
            userService.setUser($scope.user.login);
            userService.setAuth($scope.user.authorities[0].name);
            getCourses($scope.user);
        });

        $scope.selectedCourseChanged = function(course) {
            if(userService.getAuth() == 'admin') {
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
            if(userService.getAuth() == 'user') {
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

        $scope.selectedTeamChanged = function(team) {
            if(userService.getAuth() == 'admin') {
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
            if(userService.getAuth() == 'user') {
                teamService.setTeam(team);
                $http({
                    url: './student_data',
                    method: "GET",
                    headers: {'course': courseService.getCourse(), 'team': teamService.getTeam(), 'email': $scope.user.email}
                }).then(function (response) {
                    console.log("Worked!");
                    console.log(response.data);
                    $scope.students = response.data;
                });

            }

        }
        $scope.selectedStudentChanged = function(email, full_name) {
            studentService.setStudentEmail(email);
            studentService.setStudentName(full_name);
        }
    }])

    .controller('TabController', ['$scope', function($scope) {
        $scope.tab = 1;

        $scope.setTab = function (newTab) {
            $scope.tab = newTab;
        };

        $scope.isSet = function (tabNum) {
            return $scope.tab === tabNum;
        };
    }])
    .controller('RegistrationController', [ '$scope', '$location', '$http', '$window', function($scope, $location, $http, $window) {
        $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";

        $scope.adminCheckbox = {
            value : true
        };

        $scope.sendRegistration = function() {
            $http({
                url : './user',
                method : "POST",
                headers : {'first_name' : $scope.firstName, 'family_name' : $scope.familyName,  'email' : $scope.email,
                    'password' : $scope.password, 'admin' : $scope.adminCheckbox.value }

            }).then(function(response) {
                console.log("Worked!");
                $scope.responseData = console.log(response.data);
                $scope.message = "User Successfully Registered";
                $window.alert($scope.message);
                $location.path('/login');
            }, function(response) {
                //fail case
                console.log("Didn't work");
                //console.log(response);
                $scope.responseData = console.log(response.data);
                $scope.message = "User Not Registered, Duplicate User or Incorrect Information";
                $window.alert($scope.message);
            });

        };

    }])
    .controller('CourseController', ['$scope', '$routeParams', '$location', 'courseService', 'userService', '$http', function ($scope, $routeParams, $location,  courseService, userService,$http) {
        if($routeParams.course_id != null){
            $scope.courseid=$routeParams.course_id;
        }else {
            $scope.courseid = "none";
        }
        console.log("course: " + $scope.courseid);

        $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";
        $http({
            url: './check_courseaccess',
            method: "GET",
            headers: {'course': $scope.courseid, 'login' : userService.getUser(), 'auth': userService.getAuth()}
        }).then(function (response) {
            console.log("Worked!");
            console.log(response.data);
            if(response.data == false){
                $location.path('/home');
            } else{
                getQuickWeightFreq();
            }
        });

        function getQuickWeightFreq() {
            $http({
                url: './taiga/course_quickweightFreq',
                method: "GET",
                headers: {'course': $scope.courseid}
            }).then(function (response) {
                console.log("Worked!");
                console.log(response.data);
                $scope.weightFreq = response.data;
            });
        }
    }])
    .controller('TeamController', ['$scope', '$location', '$routeParams', 'courseService', 'teamService', 'userService', '$http', function ($scope, $location, $routeParams, courseService, teamService, userService, $http) {
        $scope.teamid = $routeParams.team_id;
        var course =  courseService.getCourse();
        $scope.courseid = courseService.getCourse();
        if(course == null){
            course = "none";
        }
        console.log("course: " + course);

        if($scope.teamid == null){
            $scope.teamid = "none";
        }

        console.log("team: " + $scope.teamid);

        $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";

        $http({
            url: './check_teamaccess',
            method: "GET",
            headers: {'course': course, 'team' : $scope.teamid, 'login' : userService.getUser(), 'auth': userService.getAuth()}
        }).then(function (response) {
            console.log("Worked!");
            console.log(response.data);
            if(response.data == false){
                $location.path('/home');
            } else{
                getQuickWeightFreq();
            }
        });

        function getQuickWeightFreq() {
            $http({
                url: './taiga/team_quickweightFreq',
                method: "GET",
                headers: {'course': course, 'team': $scope.teamid}
            }).then(function (response) {
                console.log("Worked!");
                console.log(response.data);
                $scope.weightFreq = response.data;
            });
        }
    }])
    .controller('StudentController', ['$scope', '$location', '$routeParams', 'courseService', 'teamService', 'studentService', 'userService', '$http', function ($scope, $location, $routeParams, courseService, teamService, studentService, userService, $http) {
        $scope.studentid = $routeParams.student_id;
        $scope.courseid = courseService.getCourse();
        $scope.teamid = teamService.getTeam();

        var course = courseService.getCourse();
        var team = teamService.getTeam();
        var studentemail = studentService.getStudentEmail();

        if(course == null) {
            course = "none";
            console.log("course: " + course);
        }

        if(team == null) {
            team = "none";
            console.log("team: " + team);
        }

        if(studentemail == null) {
            studentemail = "none";
            console.log("studentemail: " + studentemail);
        }

        $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";
        $http({
            url: './check_studentaccess',
            method: "GET",
            headers: {'course': course, 'team' : team, 'login' : userService.getUser(), 'auth': userService.getAuth(), 'studentemail': studentemail, 'fullname': $scope.studentid}
        }).then(function (response) {
            console.log("Worked!");
            console.log(response.data);
            if(response.data == false){
                $location.path('/home');
            } else{
                getQuickWeightFreq();
            }
        });

        function getQuickWeightFreq() {
            $http({
                url: './taiga/student_quickweightFreq',
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
                url : './taiga/student_activity',
                method : "GET",
                headers : {'course' : course, 'team' : team, 'email' : studentemail}
            }).then(function(response) {
                console.log("Worked!");
                console.log(response.data);
                $scope.studentActivity = response.data;
            });
        }


    }])
;
