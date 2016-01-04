appCtrllers
    .controller('transactionCtrl', ['$scope', 'FinApp', '$state', '$stateParams',
        function ($scope, FinApp, $state, $stateParams) {
            $scope.transactionId = $stateParams.transactionId;

            FinApp.getTransaction($scope.transactionId).then(function (res) {
                $scope.transaction = res.data;
                $scope.transaction.date = new Date($scope.transaction.date);
                var pos = {
                    'lat': $scope.transaction.location.latitude,
                    'lng': $scope.transaction.location.longitude
                };               
                $scope.$on('mapInitialized', function (event, map) {
                    $scope.map = map;
                    $scope.map.setCenter(pos);
                });                
                $scope.places = $scope.transaction.places;                
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
