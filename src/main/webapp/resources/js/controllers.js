'use strict';

myapp.service('courseService', function() {
    var course = "";

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
        var team = "";

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
        var studentEmail = "";
        var studentName = "";

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

    }).controller('InstructorController', ['$scope', '$location', '$http', 'courseService', 'teamService', 'studentService', '$window', '$routeParams',
    function($scope, $location, $http, courseService, teamService, studentService, $window) {

    $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";

    function getCourses(user) {
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

    $http({
        url : './current_user',
        method : "GET"
    }).then(function(response) {
        console.log("Worked!");
        console.log(response.data);
        $scope.user = response.data;
        getCourses($scope.user);
    });

    $scope.selectedCourseChanged = function(course) {
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

    $scope.selectedTeamChanged = function(team) {
        teamService.setTeam(team);
        $http({
            url: './team_students',
            method: "GET",
            headers: {'course': courseService.getCourse(), 'team' : teamService.getTeam()}
        }).then(function (response) {
            console.log("Worked!");
            console.log(response.data);
            $scope.students = response.data;
        });
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
    .controller('CourseController', ['$scope', '$routeParams', 'courseService', '$http', function ($scope, $routeParams, courseService, $http) {
    $scope.courseid=$routeParams.course_id;

    $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";

    $http({
        url : './taiga/course_quickweightFreq',
        method : "GET",
        headers : {'course' : $scope.courseid}
    }).then(function(response) {
        console.log("Worked!");
        console.log(response.data);
        $scope.weightFreq = response.data;
    });
}])
    .controller('TeamController', ['$scope', '$routeParams', 'courseService', 'teamService', '$http', function ($scope, $routeParams, courseService, teamService, $http) {
        $scope.courseid = courseService.getCourse();
        $scope.teamid = $routeParams.team_id;

        $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";

        $http({
            url : './taiga/team_quickweightFreq',
            method : "GET",
            headers : {'course' : $scope.courseid, 'team' : $scope.teamid}
        }).then(function(response) {
            console.log("Worked!");
            console.log(response.data);
            $scope.weightFreq = response.data;
        });
    }])
    .controller('StudentController', ['$scope', '$routeParams', 'courseService', 'teamService', 'studentService', '$http', function ($scope, $routeParams, courseService, teamService, studentService, $http) {
        $scope.courseid = courseService.getCourse();
        $scope.teamid = teamService.getTeam();
        $scope.studentid = $routeParams.student_id;;
        $scope.studentemail = studentService.getStudentEmail();

        $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";

        $http({
            url : './taiga/student_quickweightFreq',
            method : "GET",
            headers : {'course' : $scope.courseid, 'team' : $scope.teamid, 'email' : $scope.studentemail}
        }).then(function(response) {
            console.log("Worked!");
            console.log(response.data);
            $scope.weightFreq = response.data;
            getTaigaActivity();
        });

        function getTaigaActivity() {
            $http({
                url : './taiga/student_activity',
                method : "GET",
                headers : {'course' : $scope.courseid, 'team' : $scope.teamid, 'email' : $scope.studentemail}
            }).then(function(response) {
                console.log("Worked!");
                console.log(response.data);
                $scope.studentActivity = response.data;
            });
        }


    }])
    ;
