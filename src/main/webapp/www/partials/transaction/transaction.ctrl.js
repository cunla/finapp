(function () {
    angular.module('app.controllers')
        .controller('transactionCtrl', ['$scope', 'fin', '$state', '$stateParams', transactionCtrl]);

    function transactionCtrl($scope, fin, $state, $stateParams) {
        $scope.transactionId = $stateParams.transactionId;

        fin.getTransaction($scope.transactionId).then(function (res) {
            $scope.transaction = res.data;
            $scope.groupAccounts = $scope.transaction.groupAccounts;
            $scope.groupCategories = $scope.transaction.groupCategories;
            $scope.transaction.date = new Date($scope.transaction.date);
            if ($scope.transaction.location) {
                var pos = {
                    'lat': $scope.transaction.location.latitude,
                    'lng': $scope.transaction.location.longitude
                };
            };

            $scope.places = $scope.transaction.places;
        });
        $scope.$on('mapInitialized', function (event, map) {
            $scope.map = map;
            //$scope.map.setCenter(pos);
        });
        //fin.getGroups().then(function (data) {
        //    $scope.accounts = data[0].accounts;
        //});
        //fin.getTargets().then(function (data) {
        //    $scope.targets = data;
        //});
        $scope.saveTransaction = function () {
            fin.updateTransaction($scope.transaction.id, $scope.transaction).then(function (d) {
                //console.log("Saving " + JSON.stringify(d));
                window.history.back();
            })
        };
        $scope.goBack = function () {
            window.history.back();
        }
    }
})();
