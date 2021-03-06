(function () {
//var appServices = angular.module('app.services', []);
    angular.module('app.controllers', ['ngMap', 'ionic-datepicker', 'jett.ionic.filter.bar']);
    var app = angular.module('app', ['ionic', 'app.controllers']);

    angular.module('app')
        .run(function ($rootScope, $state, fin) {
            // Check login session
            $rootScope.$on('$stateChangeStart', function (event, next, current) {
                fin.currentUser().then(function (res) {
                    $rootScope.user = res.data;
                    $rootScope.user.birthday = new Date($rootScope.user.birthday);
                    $rootScope.groupId = $state.params.groupId || $rootScope.user.lastGroupId;
                    if (!$rootScope.user) {
                        // user not logged in | redirect to login
                        if (next.name !== "login") {
                            event.preventDefault();
                            $state.go('login');
                        }
                    } else if (next.name === "login") {
                        event.preventDefault();
                        $state.go('menu.profile');
                    }
                }, function (err) {
                    event.preventDefault();
                    $state.go('login');
                });
            });
        })
        .factory('errorInterceptor', [function () {
            var responseInterceptor = {
                request: function (config) {
                    config.requestTimestamp = new Date().getTime();
                    return config;
                },
                response: function (response) {
                    response.config.responseTimestamp = new Date().getTime();
                    if (response.status >= 400) {
                        console.log(response.data);
                    }
                    return response;
                }
            };

            return responseInterceptor;
        }])
        .config(['$httpProvider', function ($httpProvider) {
            $httpProvider.interceptors.push('errorInterceptor');
        }])
        // Routes
        .config(function ($stateProvider, $urlRouterProvider) {
            // setup states
            $stateProvider
                .state('login', {
                    url: "/login",
                    templateUrl: "partials/login/login.html",
                    controller: 'loginCtrl'
                })
                .state('menu', {
                    url: "/f",
                    abstract: true,
                    templateUrl: "partials/menu/menu.html",
                    controller: "menuCtrl"
                })
                .state('menu.about', {
                    url: "/about",
                    views: {
                        'appContent': {
                            templateUrl: "partials/about/about.html",
                            controller: "About"
                        }
                    }
                })
                .state('menu.profile', {
                    url: "/profile",
                    views: {
                        'appContent': {
                            templateUrl: "partials/profile/profile.html",
                            controller: "profileCtrl"
                        }
                    }
                })
                .state('menu.group', {
                    url: "/group/:groupId",
                    views: {
                        'appContent': {
                            templateUrl: "partials/group/group.html",
                            controller: "Group"
                        }
                    }
                })
                .state('menu.accounts', {
                    url: "/accounts/:groupId",
                    views: {
                        'appContent': {
                            templateUrl: "partials/accounts/accounts.html",
                            controller: "accountsCtrl"
                        }
                    }
                })
                .state('menu.home', {
                    url: '/home/:groupId',
                    views: {
                        'appContent': {
                            templateUrl: 'partials/home/home.html',
                            controller: 'homeCtrl'
                        }
                    }
                })
                .state('menu.reports', {
                    url: '/report/:groupId',
                    views: {
                        'appContent': {
                            templateUrl: 'partials/category/category.html',
                            controller: 'catCtrl'
                        }
                    }
                })
                .state('menu.transaction', {
                    url: '/transaction/:transactionId',
                    views: {
                        'appContent': {
                            templateUrl: 'partials/transaction/transaction.html',
                            controller: 'transactionCtrl'
                        }
                    }
                })
            ;
            // default route
            $urlRouterProvider.otherwise("/login");

        });
})();
