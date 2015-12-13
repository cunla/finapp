appCtrllers
    .controller('accountCtrl', ['$scope', '$state', '$stateParams', '$rootScope', '$http', '$ionicHistory', function ($scope, $state, $stateParams, $rootScope, $http, $ionicHistory) {
        $scope.doRefresh = function () {
            //Get account transactions (/account/{id})
            $scope.accountId = $stateParams.accountId;
            $http.get('data/account.json').success(function (data) {
                    $scope.account = data;
                })
                .finally(function () {
                    $scope.$broadcast('scroll.refreshComplete');
                });
        };
        $scope.doRefresh();
        $scope.editTransaction = function (transactionId) {
            $state.go('transaction', {transactionId: transactionId});
        };
        $scope.deleteTransaction = function (transaction) {
            //TODO
            var index = $scope.account.transactions.indexOf(transaction);
            $scope.account.transactions.splice(index, 1);
        };
        $scope.newTransaction = function () {
            //TODO
            $http.get('data/account.json').success(function (data) {
                var transaction = data.transactions.length + 1;
                $scope.editTransaction(transaction);
            })
        };
        $scope.goBack = function () {
            window.history.back();
        };
        $scope.getLocation = function (loc) {

            return new google.maps.LatLng(loc.lat, loc.lon);
        };
    }]);
