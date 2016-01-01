appCtrllers
    .controller('menuCtrl', ['$scope', 'FinApp', '$state', '$stateParams', '$rootScope', '$http', '$ionicSideMenuDelegate',
        function ($scope, FinApp, $state, $stateParams, $rootScope, $http, $ionicSideMenuDelegate) {
            $scope.currentState = $state.current.name;

            $scope.toggleLeft = function () {
                $ionicSideMenuDelegate.toggleLeft();
            };
        }])

