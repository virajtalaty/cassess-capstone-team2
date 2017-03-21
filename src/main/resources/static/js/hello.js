angular.module('hello', [ 'ngRoute' ]).config(function($routeProvider, $httpProvider) {

    $routeProvider.when('/', {
        templateUrl : 'partials/home.html',
        controller : 'home',
        controllerAs: 'controller'
    }).when('/login', {
        templateUrl : 'partials/login.html',
        controller : 'navigation',
        controllerAs: 'controller'
    }).when('/dashboard', {
        templateUrl : 'partials/dashboard.html',
        controller : 'dashboard',
        controllerAs: 'controller'
    }).when('/about', {
        templateUrl : 'partials/about.html',
        controller : 'about',
        controllerAs: 'controller'
    }).when('/slack', {
    	templateUrl : 'partials/slack-user.html',
    	controller : 'slack',
    	controllerAs : 'controller'
    }).when('/taiga', {
        templateUrl : 'partials/main.html',
        controller : 'taiga',
        controllerAs : 'controller'
    }).when('/charts', {
        templateUrl : 'partials/charts.html',
        controller : 'charts',
        controllerAs : 'controller'
    }).otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

    }).controller('navigation',

    function($rootScope, $http, $location) {

        var self = this

        var authenticate = function(credentials, callback) {

            var headers = credentials ? {authorization : "Basic "
                + btoa(credentials.username + ":" + credentials.password)
                } : {};

            $http.get('user', {headers : headers}).then(function(response) {
                if (response.data.name) {
                    $rootScope.authenticated = true;
                } else {
                    $rootScope.authenticated = false;
                }
                callback && callback();
            }, function() {
                $rootScope.authenticated = false;
                callback && callback();
            });

        }

        authenticate();
        self.credentials = {};
        self.login = function() {
            authenticate(self.credentials, function() {
                if ($rootScope.authenticated) {
                    $location.path("/");
                    self.error = false;
                } else {
                    $location.path("/login");
                    self.error = true;
                }
            })
        };

        self.logout = function() {
            $http.post('logout', {}).finally(function() {
                $rootScope.authenticated = false;
                $location.path("/");
            });
        }
    }).controller('home', function($http) {
        var self = this;
        $http.get('/resource/').then(function(response) {
            self.greeting = response.data;
        })
    }).controller('slack', function($scope, $http) {
    $http.get('/slack_resource/').then(function (response) {
        $scope.slackData = response.data;
    })
}).controller("taiga", [ '$scope', '$http', function($scope, $http) {

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

        $scope.sendPost = function() {
            console.log($scope.name);
            $http({
                url : './taigaTasks',
                method : "POST",
                headers: {'name' : $scope.selectedStudent.value.full_name}
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

    } ]).controller('charts', function($scope, $http) {
        var self = this;
        $http.get("/charts/").then(function (response) {
            self.chartData = response.data;
        })
    });
