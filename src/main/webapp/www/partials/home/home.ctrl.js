appCtrllers
    .controller('homeCtrl', ['$scope', 'FinApp', '$state', '$stateParams', '$rootScope', '$ionicPopup',
        function ($scope, FinApp, $state, $stateParams, $rootScope, $ionicPopup) {
            $scope.t = {};
            $scope.doRefresh = function () {
                FinApp.getTransactions().then(function (results) {
                    $scope.transactions = results;
                    $scope.sumAll = 0;
                    $scope.transactions.forEach(function (t) {
                        $scope.sumAll += t.amount;
                    })
                }).finally(function () {
                    $scope.$broadcast('scroll.refreshComplete');
                })
            }
            $scope.doRefresh();
            $scope.createTransaction = function (t) {
                FinApp.newTransaction((t.plusSign ? t.amount : -t.amount)).then(function (res) {
                    console.log("Save transaction " + JSON.stringify(res));
                    $scope.transactions.push(res);
                    $scope.doRefresh();
                })
                $scope.t = {};
            }
            $scope.deleteTransaction = function (t) {
                var confirmPopup = $ionicPopup.confirm({
                    title: 'Delete transaction',
                    template: 'Are you sure you delete this transaction?',
                    okText: 'Delete',
                    okType: 'button-assertive',
                    cancelText: 'No'
                });
                confirmPopup.then(function (res) {
                    if (res) {
                        console.log('Deleting transaction ' + JSON.stringify(t));
                        FinApp.deleteTransaction(t.objectId).then(function () {
                            var i = $scope.transactions.indexOf(t);
                            $scope.transactions.splice(i, 1);
                        });
                    } else {
                        console.log('You are not sure');
                    }
                });
            }
        }]);
