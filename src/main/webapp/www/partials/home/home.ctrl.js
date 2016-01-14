(function () {
    angular.module('app.controllers')
        .controller('homeCtrl', ['$scope', 'fin', '$state', '$stateParams', '$rootScope', '$ionicPopup', homeCtrl])
    function homeCtrl($scope, fin, $state, $stateParams, $rootScope, $ionicPopup) {
        var groupId = $stateParams.groupId;
        $scope.t = {};
        $scope.doRefresh = refresh;
        $scope.createTransaction = createTransaction;
        $scope.toggleDefaultFilter = toggleDefaultFilter;
        $scope.deleteTransaction = deleteTransaction;

        $scope.doRefresh();

        function createTransaction(t) {
            t.date = new Date();
            t.amount = (t.plusSign ? t.amount : -t.amount);
            $scope.sumAll = $scope.sumAll + t.amount;
            $scope.transactions.unshift(t);
            fin.newTransaction(groupId, t.amount).then(function (res) {
                res.date = new Date(res.date);
                $scope.transactions[0] = res;
                //$scope.doRefresh();
            })
            $scope.t = {};
        }

        function deleteTransaction(t) {
            var confirmPopup = $ionicPopup.confirm({
                title: 'Delete transaction',
                template: 'Are you sure you delete this transaction?',
                okText: 'Delete',
                okType: 'button-assertive',
                cancelText: 'No'
            });
            confirmPopup.then(function (res) {
                if (res) {
                    //console.log('Deleting transaction ' + JSON.stringify(t));
                    fin.deleteTransaction(t.id).then(function () {
                        var i = $scope.transactions.indexOf(t);
                        $scope.transactions.splice(i, 1);
                    });
                }
            });
        }

        function refresh() {
            fin.getTransactions(groupId).then(function (results) {
                $scope.transactions = results.data.content;
                $scope.sumAll = 0;
                $scope.noData = 0;
                if ($scope.transactions) {
                    $scope.transactions.forEach(function (t) {
                        t.date = new Date(t.date);
                        $scope.sumAll += t.amount;
                        if (!t.categoryId) {
                            ++$scope.noData;
                        }
                    })
                } else {
                    $scope.transactions = [];
                }
            }).finally(function () {
                $scope.$broadcast('scroll.refreshComplete');
            })
        }

        function toggleDefaultFilter() {
            if (!$scope.filterT) {
                $scope.filterT = {categoryId: null};
            } else {
                $scope.filterT = null;
            }
        }
    }


})();
