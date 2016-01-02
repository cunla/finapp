appCtrllers

    .controller('menuCtrl', ['$scope', 'FinApp', '$state', '$stateParams', '$rootScope', '$http', '$ionicSideMenuDelegate',
        function ($scope, FinApp, $state, $stateParams, $rootScope, $http, $ionicSideMenuDelegate) {

            $scope.currentState = $state.current.name;
            var refresh = function () {
                $ionicSideMenuDelegate.toggleLeft();
                $scope.groupId = $stateParams.groupId;
            }

            function changeState(st) {
                $state.go(st);
                $scope.currentState = $state.current.name;
            }

            $scope.goToProfile = function () {
                changeState('menu.profile');
                refresh();
            }
            $scope.goToReports = function () {
                changeState('menu.reports');
                refresh();
            }
            $scope.goToSettings = function () {
                changeState('menu.settings');
                refresh();
            }
            $scope.goToGroup = function () {
                changeState('menu.home', {"groupId": $scope.groupId});
                refresh();
            }
            refresh();
        }])
