appCtrllers
    .controller('transactionCtrl', ['$scope', 'FinApp', '$state', '$stateParams', 'Places',
        function ($scope, FinApp, $state, $stateParams, Places) {
            $scope.transactionId = $stateParams.transactionId;

            FinApp.getTransaction($scope.transactionId).then(function (res) {
                $scope.transaction = res.data;
                $scope.transaction.date = new Date($scope.transaction.date);
                $scope.pos = {
                    'lat': $scope.transaction.location.latitude,
                    'lng': $scope.transaction.location.longitude
                };
                $scope.transaction.actualDate = new Date($scope.transaction.date);
                $scope.$on('mapInitialized', function (event, map) {
                    $scope.map = map;
                    $scope.map.setCenter($scope.pos);
                });
            });
            //FinApp.getGroups().then(function (data) {
            //    $scope.accounts = data[0].accounts;
            //});
            //FinApp.getTargets().then(function (data) {
            //    $scope.targets = data;
            //});
            $scope.saveTransaction = function () {
                FinApp.updateTransaction($scope.transaction.id, $scope.transaction).then(function (d) {
                    //console.log("Saving " + JSON.stringify(d));
                    window.history.back();
                })
            };
            $scope.goBack = function () {
                window.history.back();
            }
        }]);
