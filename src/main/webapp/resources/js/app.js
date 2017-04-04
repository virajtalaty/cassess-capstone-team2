'use strict';

var myapp = angular
    .module('myApp', ['ngResource', 'ngRoute', 'swaggerUi', 'http-auth-interceptor', 'ngAnimate', 'angular-spinkit', 'ng-fusioncharts']);


myapp.constant('USER_ROLES', {
    all: '*',
    admin: 'admin',
    user: 'user'
});


myapp.config(function ($routeProvider, USER_ROLES) {

    $routeProvider.when("/home", {
        templateUrl: "partials/home.html",
        controller: 'HomeController',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/', {
        redirectTo: '/home'
    }).when('/apiDoc', {
        templateUrl: 'partials/apiDoc.html',
        controller: 'ApiDocController',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.admin]
        }
    }).when('/login', {
        templateUrl: 'partials/login.html',
        controller: 'LoginController',
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/register', {
        templateUrl: 'partials/register.html',
        controller: 'RegistrationController',
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/taiga_admin', {
            templateUrl : 'partials/taigaAdmin.html',
            controller : 'TaigaAdmin',
            access: {
                loginRequired: true,
                authorizedRoles: [USER_ROLES.admin]
            }
    }).when('/githubData', {
        templateUrl: 'partials/githubData.html',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/slackData', {
        templateUrl: 'partials/slackData.html',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/about', {
        templateUrl : 'partials/about.html',
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/loading', {
        templateUrl: 'partials/loading.html',
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when("/logout", {
        template: " ",
        controller: "LogoutController",
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when("/error/:code", {
        templateUrl: "partials/error.html",
        controller: "ErrorController",
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).otherwise({
        redirectTo: '/error/404',
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    });
});

myapp.run(function ($rootScope, $location, $http, AuthSharedService, Session, USER_ROLES, $q, $timeout) {

    $rootScope.$on('$routeChangeStart', function (event, next) {

        if(next.originalPath === "/login" && $rootScope.authenticated) {
            event.preventDefault();
        } else if (next.access && next.access.loginRequired && !$rootScope.authenticated) {
            event.preventDefault();
            $rootScope.$broadcast("event:auth-loginRequired", {});
        } else if (next.access && !AuthSharedService.isAuthorized(next.access.authorizedRoles)) {
            event.preventDefault();
            $rootScope.$broadcast("event:auth-forbidden", {});
        }
    });

    $rootScope.$on('$routeChangeSuccess', function (scope, next, current) {
        $rootScope.$evalAsync(function () {
            $.material.init();
        });
    });

    // Call when the the client is confirmed
    $rootScope.$on('event:auth-loginConfirmed', function (event, data) {
        console.log('login confirmed start ' + data);
        $rootScope.loadingAccount = false;
        var nextLocation = ($rootScope.requestedUrl ? $rootScope.requestedUrl : "/home");
        var delay = ($location.path() === "/loading" ? 1500 : 0);

        $timeout(function () {
            Session.create(data);
            $rootScope.account = Session;
            $rootScope.authenticated = true;
            $location.path(nextLocation).replace();
        }, delay);

    });

    // Call when the 401 response is returned by the server
    $rootScope.$on('event:auth-loginRequired', function (event, data) {
        if ($rootScope.loadingAccount && data.status !== 401) {
            $rootScope.requestedUrl = $location.path()
            $location.path('/loading');
        } else {
            Session.invalidate();
            $rootScope.authenticated = false;
            $rootScope.loadingAccount = false;
            $location.path('/login');
        }
    });

    // Call when the 403 response is returned by the server
    $rootScope.$on('event:auth-forbidden', function (rejection) {
        $rootScope.$evalAsync(function () {
            $location.path('/error/403').replace();
        });
    });

    // Call when the user logs out
    $rootScope.$on('event:auth-loginCancelled', function () {
        $location.path('/login').replace();
    });

    // Get already authenticated user account
    AuthSharedService.getAccount();
}).controller('charts', function($scope, $http) {
        var self = this;
        $http.get("/charts/").then(function (response) {
            self.chartData = response.data;
            /*$scope.myDataSource = {
                chart: {
                    caption: "GitHubCommits for tjjohn1",
                    subCaption: "Displays Commits By Week",
                    theme: "fint"
                },
                data: response.data
            };*/
        });
        //$scope.myDataSource = {};

        //Define the `myDataSource` scope variable.
            $scope.myDataSource = {
                chart: {
                    caption: "Harry's SuperMart",
                    subCaption: "Top 5 stores in last month by revenue",
                    numberPrefix: "$",
                    theme: "fint"
                },
                data:[{
                    label: "Bakersfield Central",
                    value: "880000"
                },
                {
                    label: "Garden Groove harbour",
                    value: "730000"
                },
                {
                    label: "Los Angeles Topanga",
                    value: "590000"
                },
                {
                    label: "Compton-Rancho Dom",
                    value: "520000"
                },
                {
                    label: "Daly City Serramonte",
                    value: "330000"
                }]
            };
    });





