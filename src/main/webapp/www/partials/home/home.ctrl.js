appCtrllers
    .controller('homeCtrl', ['$scope', 'FinApp', '$state', '$stateParams', '$rootScope', '$ionicPopup',
        function ($scope, FinApp, $state, $stateParams, $rootScope, $ionicPopup) {
            var groupId = $stateParams.groupId;
            $scope.t = {};
            $scope.doRefresh = function () {
                FinApp.getTransactions(groupId).then(function (results) {
                    $scope.transactions = results.data.content;
                    $scope.sumAll = 0;
                    if ($scope.transactions) {
                        $scope.transactions.forEach(function (t) {
                            t.actualDate = new Date(t.date);
                            $scope.sumAll += t.amount;
                        })
                    } else {
                        $scope.transactions = [];
                    }
                }).finally(function () {
                    $scope.$broadcast('scroll.refreshComplete');
                })
            }
            $scope.doRefresh();
            $scope.createTransaction = function (t) {
                FinApp.newTransaction(groupId, (t.plusSign ? t.amount : -t.amount)).then(function (res) {
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
                        FinApp.deleteTransaction(t.id).then(function () {
                            var i = $scope.transactions.indexOf(t);
                            $scope.transactions.splice(i, 1);
                        });
                    } else {
                        console.log('You are not sure');
                    }
                });
            }
        }])


