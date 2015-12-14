//var appServices = angular.module('app.services', []);
var appCtrllers = angular.module('app.controllers', []);
var app = angular.module('app', ['ngCookies', 'ionic', 'app.controllers']);

app.run(function ($rootScope, $cookies, $state, FinApp) {
    // Check login session
    $rootScope.$on('$stateChangeStart', function (event, next, current) {
        var userInfo = $cookies.get('userInfo');
        FinApp.currentUser().then(function (res) {
            $rootScope.user = res.data;
            $rootScope.user.birthday=new Date($rootScope.user.birthday);
            $cookies.putObject('userInfo', $rootScope.user);
            if (!$rootScope.user) {
                // user not logged in | redirect to login
                if (next.name !== "login") {
                    event.preventDefault();
                    $state.go('login');
                }
            } else if (next.name === "login") {
                event.preventDefault();
                $state.go('profile');
            }
        },function(err){
            $cookies.remove('userInfo');
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
        .state('home',{
            url:'/v2/home',
            templateUrl:'partials/home/home.html',
            controller: 'homeCtrl',
            data:{
                authenticate:true
            }
        })
        .state('transaction',{
            url:'/transaction/:transactionId',
            templateUrl: 'partials/transaction/transaction.html',
            controller: 'transactionCtrl',
            data: {
                authenticate: true
            }
        })
    ;
    // default route
    $urlRouterProvider.otherwise("/login");

});
