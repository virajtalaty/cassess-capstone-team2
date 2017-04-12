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

    })
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

    } ] )
    .controller('CourseController', function ($scope, $routeParams, $http) {
        $scope.courseid=$routeParams.course_id;

        $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";

        $http({
            url : './taigaCourses',
            method : "GET"
        }).then(function(response) {
            console.log("Worked!");
            //console.log(response.data);
            $scope.courses = response.data;
        });
    })
    .controller("TaigaAdmin", [ '$scope', '$http', function($scope, $http) {

        $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";

        $http({
            url : './taigaCourses',
            method : "GET"
        }).then(function(response) {
            console.log("Worked!");
            //console.log(response.data);
            $scope.courses = response.data;
        });

        $scope.selectedCourseChanged = function(){
            $http({
                url : './taigaTeams',
                method : "GET",
                headers: {'course' : $scope.selectedCourse.value.course}
            }).then(function(response) {
                console.log("Worked!: " + $scope.selectedCourse.value.course);
                //console.log(response.data);
                $scope.teams = response.data;
                console.log($scope.teams);
            }, function(response) {
                //fail case
                console.log("didn't work");
                //console.log(response);
                $scope.message = response;
            });

        }

        $scope.selectedTeamChanged = function(){
            $http({
                url : './taigaStudents',
                method : "GET",
                headers: {'team' : $scope.selectedTeam.value.team}
            }).then(function(response) {
                console.log("Worked!: " + $scope.selectedTeam.value.team);
                //console.log(response.data);
                $scope.students = response.data;
                console.log($scope.students);
            }, function(response) {
                //fail case
                console.log("didn't work");
                //console.log(response);
                $scope.message = response;
            });

        }

        $scope.taskProgress = function() {
            console.log($scope.name);
            $http({
                url : './taigaProgress',
                method : "POST",
                headers: {'name' : $scope.selectedStudent.value.full_name}
            }).then(function(response) {
                console.log("Worked!");
                //console.log(response.data);
                $scope.tasksRecords = null;
                $scope.tasksProgress = response.data;
            }, function(response) {
                //fail case
                console.log("didn't work");
                //console.log(response);
                $scope.message = response;
            });

        };

        $scope.taskRecords = function() {
            console.log($scope.name);
            $http({
                url : './taigaRecords',
                method : "POST",
                headers: {'name' : $scope.selectedStudent.value.full_name}
            }).then(function(response) {
                console.log("Worked!");
                //console.log(response.data);
                $scope.tasksProgress = null;
                $scope.tasksRecords = response.data;
            }, function(response) {
                //fail case
                console.log("didn't work");
                //console.log(response);
                $scope.message = response;
            });

        };

        $scope.updateTaigaProjects = function() {
            $http({
                url : './taiga/Update/Projects',
                method : "POST"
            }).then(function(response) {
                console.log("Worked!");
                //console.log(response.data);
                $scope.tasks = response.data;
            }, function(response) {
                //fail case
                console.log("didn't work");
                //console.log(response);
                $scope.message = response;
            });

        };

        $scope.updateTaigaMemberships = function() {
            $http({
                url : './taiga/Update/Memberships',
                method : "POST"
            }).then(function(response) {
                console.log("Worked!");
                //console.log(response.data);
                $scope.tasks = response.data;
            }, function(response) {
                //fail case
                console.log("didn't work");
                //console.log(response);
                $scope.message = response;
            });

        };

        $scope.updateTaigaTaskTotals = function() {
            $http({
                url : './taiga/Update/Tasks',
                method : "POST"
            }).then(function(response) {
                console.log("Worked!");
                //console.log(response.data);
                $scope.tasks = response.data;
            }, function(response) {
                //fail case
                console.log("didn't work");
                //console.log(response);
                $scope.message = response;
            });

        };

}]).controller('charts', function($scope, $http) {

    var self = this;
    /*$http.get("./GitHubIndividualCommitChart").then(function (response) {
     $scope.chartData = response.data;
     $scope.options = {
     chart: {
     type: 'lineChart',
     height: 450,
     margin : {
     top: 20,
     right: 20,
     bottom: 50,
     left: 65
     },
     x: function(d){ return d[0]; },
     y: function(d){ return d[1]; },
     color: d3.scale.category10().range(),
     useInteractiveGuideline: true,
     xAxis: {
     axisLabel: 'Weeks',
     tickFormat: function(d) {
     return d3.time.format('%m/%d/%y')(new Date(d))
     },
     showMaxMin: false,
     staggerLabels: true
     },
     yAxis: {
     axisLabel: 'Commits',
     axisLabelDistance: 0
     }
     }
     };
     $scope.data = [response.data];
     });*/

    $http.get("./TaigaBarGraph").then(function (response) {

        self.chartData = response.data;

        $scope.options = {

            chart: {

                type: 'multiBarChart',
                height: 600,

                margin : {
                    top: 20,
                    right: 20,
                    bottom: 60,
                    left: 45
                },

                x: function(d){ return d[0]; },
                y: function(d){ return d[1]; },

                clipEdge: true,
                staggerLabels: true,
                transitionDuration: 1000,

                //tooltips: true,

                //tooltipContent: function (key, x, y, e, graph) {
                //  return '<p>' + key + ': ' + y + '</p>';
                //},

                stacked: false,
                showControls: false,

                xAxis: {
                    axisLabel: 'Day',
                    showMaxMin: true,
                    tickFormat: function(d) {
                        return d3.time.format('%m/%d/%y')(new Date(d))
                    }
                },

                yAxis: {
                    axisLabel: 'Tasks',
                    axisLabelDistance: 100
                }
            }
        };

        $scope.data = response.data;

    });

});
