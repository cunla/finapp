(function () {
    angular.module('app.controllers')
        .controller('menuCtrl', ['$scope', 'fin', '$state', '$rootScope', '$http', '$ionicSideMenuDelegate', menuCtrl]);

    function menuCtrl($scope, fin, $state, $rootScope, $http, $ionicSideMenuDelegate) {

        $scope.currentState = $state.current.name;
        var refresh = function () {
            $ionicSideMenuDelegate.toggleLeft();
            $scope.groupId = $state.params.groupId;
        };


        function changeState(st) {
            $state.go(st);
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
        refresh();
    }
})();
