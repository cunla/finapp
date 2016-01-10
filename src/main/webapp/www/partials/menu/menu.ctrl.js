(function () {
    angular.module('app.controllers')
        .controller('menuCtrl', ['$scope', 'fin', '$state', '$rootScope', '$http', '$ionicSideMenuDelegate', menuCtrl]);

    function menuCtrl($scope, fin, $state, $rootScope, $http, $ionicSideMenuDelegate) {

        $scope.currentState = $state.current.name;
        var refresh = function () {
            $ionicSideMenuDelegate.toggleLeft();
            if ($state.params.groupId && $state.params.groupId != "") {
                $scope.groupId = $state.params.groupId;
            } else {
                $scope.groupId = $rootScope.groupId;
            }

        };


        function changeState(st, params) {
            $state.go(st, params);
            $scope.currentState = st;
            refresh();
        };

        $scope.goToProfile = function () {
            changeState('menu.profile');
        }
        $scope.goToReports = function () {
            changeState('menu.reports', {"groupId": $scope.groupId});
        }
        $scope.goToSettings = function () {
            changeState('menu.settings');
        }
        $scope.goToGroup = function () {
            changeState('menu.home', {"groupId": $scope.groupId});
        }
        $scope.goToAccounts = function () {
            changeState('menu.accounts', {"groupId": $scope.groupId});
        }
        $scope.goToAbout = function () {
            changeState('menu.about');
        }
        refresh();
    }
})();
