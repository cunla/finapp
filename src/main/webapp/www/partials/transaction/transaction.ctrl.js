appCtrllers
    .controller('transactionCtrl', ['$scope', 'Finance', '$state', '$stateParams', '$rootScope', '$http', '$ionicHistory',
        function ($scope, Finance, $state, $stateParams, $rootScope, $http, $ionicHistory) {
            $scope.transactionId = $stateParams.transactionId;

            Finance.getTransaction($scope.transactionId).then(function (data) {
                $scope.transaction = data;
                $scope.pos={'lat':$scope.transaction.location.latitude,'lng':$scope.transaction.location.longitude};
                $scope.transaction.time = new Date($scope.transaction.actualDate.iso);
                $scope.$on('mapInitialized', function(event, map) {
                    $scope.map = map;
                    $scope.map.setCenter($scope.pos);
                });
            });
            Finance.getGroups().then(function(data){
                $scope.accounts = data[0].accounts;
            });
            Finance.getTargets().then(function(data){
                $scope.targets = data;
            });
            $scope.saveTransaction = function () {
                Finance.saveTransaction($scope.transaction).then(function(d){
                    console.log("Saving " + JSON.stringify(d));
                    $state.go("home");
                })
            };
            $scope.goBack = function () {
                window.history.back();
            }
        }]);
