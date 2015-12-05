angular.module('login-app', ['ionic', 'ionic-material', 'ionMdInput', 'starter.services'])
    .run(['$rootScope', '$window', '$ionicPlatform',
        function ($rootScope, $window, $ionicPlatform) {
            $ionicPlatform.ready(function () {
                // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
                // for form inputs)
                if (window.cordova && window.cordova.plugins.Keyboard) {
                    cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
                }
                if (window.StatusBar) {
                    // org.apache.cordova.statusbar required

                    StatusBar.styleDefault();
                }
            });
            $rootScope.user = {};

        }])
    .controller('LoginCtrl', ['$scope', '$timeout', '$stateParams', 'ionicMaterialInk', '$state', 'fbService',
        function ($scope, $timeout, $stateParams, ionicMaterialInk, $state, fbService) {
            $scope.$parent.clearFabs();
            $timeout(function () {
                $scope.$parent.hideHeader();
            }, 0);
            ionicMaterialInk.displayEffect();
            $scope.login = function (user, password) {
                console.log("Logged in with " + user + "/" + password);
                $state.go('app.profile');
            };

            $scope.facebookLogin = function () {
                fbService.login();
            }
        }]);
