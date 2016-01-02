appCtrllers
    .controller('menuCtrl', ['$scope', 'FinApp', '$state', '$stateParams', '$rootScope', '$http', '$ionicSideMenuDelegate',
        function ($scope, FinApp, $state, $stateParams, $rootScope, $http, $ionicSideMenuDelegate) {

            var refresh = function () {
                $ionicSideMenuDelegate.toggleLeft();
                $scope.currentState = $state.current.name;
                $scope.groupId = $stateParams.groupId;
            }
            $scope.goToProfile = function () {
                $state.go('menu.profile');
                refresh();
            }
            $scope.goToReports = function () {
                $state.go('menu.reports');
                refresh();
            }
            $scope.goToSettings = function () {
                $state.go('menu.settings');
                refresh();
            }
            $scope.goToGroup = function () {
                $state.go('menu.home', {"groupId": $scope.groupId});
                refresh();
            }
            refresh();
        }])

