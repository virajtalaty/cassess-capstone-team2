'use strict';

var myapp = angular
    .module('myApp', ['ngResource', 'ngRoute', 'swaggerUi', 'http-auth-interceptor', 'ngAnimate', 'angular-spinkit']);

myapp.constant('USER_ROLES', {
    all: '*',
    admin: 'admin',
    user: 'user'
});

myapp.config(function($routeProvider, USER_ROLES) {

    $routeProvider.when('/', {
        templateUrl : 'partials/home.html',
        controller : 'HomeController',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/login', {
        templateUrl : 'partials/login.html',
        controller : 'navigation',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/dashboard', {
        templateUrl : 'partials/dashboard.html',
        controller : 'dashboard',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.admin]
        }
    }).when('/about', {
        templateUrl : 'partials/about.html',
        controller : 'about',
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/slack', {
    	templateUrl : 'partials/slack-user.html',
    	controller : 'slack',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.admin]
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


});