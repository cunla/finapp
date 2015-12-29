//var appServices = angular.module('app.services', []);
var appCtrllers = angular.module('app.controllers', ['ngMap']);
var app = angular.module('app', ['ngCookies', 'ionic', 'app.controllers']);

app.run(function ($rootScope, $cookies, $state, FinApp) {
    // Check login session
    $rootScope.$on('$stateChangeStart', function (event, next, current) {
        FinApp.currentUser().then(function (res) {
            $rootScope.user = res.data;
            $rootScope.user.birthday = new Date($rootScope.user.birthday);
            if (!$rootScope.user) {
                // user not logged in | redirect to login
                if (next.name !== "login") {
                    event.preventDefault();
                    $state.go('login');
                }
            } else if (next.name === "login") {
                event.preventDefault();
                if ($rootScope.user.lastGroupId) {
                    $state.go('home', {"groupId": $rootScope.user.lastGroupId});
                } else {
                    $state.go('profile');
                }
            }
        }, function (err) {
            event.preventDefault();
            $state.go('login');
        });
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
        })
        .state('account', {
            url: "/account/:account",
            templateUrl: "partials/account/account.html",
            controller: "accountCtrl"
        })
        .state('home', {
            url: '/home/:groupId',
            templateUrl: 'partials/home/home.html',
            controller: 'homeCtrl'
        })
        .state('transaction', {
            url: '/transaction/:transactionId',
            templateUrl: 'partials/transaction/transaction.html',
            controller: 'transactionCtrl'
        })
    ;
    // default route
    $urlRouterProvider.otherwise("/login");

});
