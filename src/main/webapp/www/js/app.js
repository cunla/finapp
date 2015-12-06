//var appServices = angular.module('app.services', []);
var appCtrllers = angular.module('app.controllers', []);
var app = angular.module('app', ['ngCookies', 'ionic', 'app.controllers']);

app.run(function ($rootScope, $cookies, $state) {
    // Check login session
    $rootScope.$on('$stateChangeStart', function (event, next, current) {
        var userInfo = $cookies.get('userInfo');
        if (!userInfo) {
            // user not logged in | redirect to login
            if (next.name !== "login") {
                // not going to #welcome, we should redirect now
                event.preventDefault();
                $state.go('login');
            }
        } else if (next.name === "login") {
            event.preventDefault();
            $state.go('profile');
        }
    });
});

// Routes
app.config(function ($stateProvider, $urlRouterProvider) {
    // setup states
    $stateProvider
        .state('login', {
            url: "/login",
            templateUrl: "partials/login/login.html",
            controller: 'loginCtrl'
        })
        .state('profile', {
            url: "/profile",
            templateUrl: "partials/profile/profile.html",
            controller: "profileCtrl"
        });
    // default route
    $urlRouterProvider.otherwise("/login");

});
