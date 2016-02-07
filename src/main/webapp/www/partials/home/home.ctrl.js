(function () {
    angular.module('app.controllers')
        .controller('homeCtrl', ['$scope', 'fin', '$state', '$stateParams', '$rootScope', '$ionicPopup', homeCtrl])
    function homeCtrl($scope, fin, $state, $stateParams, $rootScope, $ionicPopup) {
        var groupId = $stateParams.groupId;
        $scope.t = {};
        $scope.transactions = [];
        $scope.page = -1;
        $scope.totalPages = 1;
        $scope.size = 20;
        $scope.moreData = false;
        $scope.doRefresh = refresh;
        $scope.createTransaction = createTransaction;
        $scope.toggleDefaultFilter = toggleDefaultFilter;
        $scope.deleteTransaction = deleteTransaction;
        $scope.loadMore = loadMore;
        $scope.loadMore();

        function createTransaction(t) {
            t.date = new Date();
            t.amount = (t.plusSign ? t.amount : -t.amount);
            $scope.sumAll = $scope.sumAll + t.amount;
            $scope.transactions.unshift(t);
            fin.newTransaction(groupId, t.amount).then(function (res) {
                res.date = new Date(res.date);
                $scope.transactions[0] = res;
//                 $scope.doRefresh();
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

        function loadMore() {
            if ($scope.page < $scope.totalPages) {
                $scope.page = $scope.page + 1;
                refresh($scope.page, $scope.size);
            }
        }

        function refresh(page, size) {
            fin.getTransactions(groupId, page, size).then(function (results) {
                $scope.transactions = $scope.transactions.concat(results.data.content);
                $scope.page = results.data.number;
                $scope.totalPages = results.data.totalPages;
                $scope.moreData = (results.data.totalPages > $scope.page + 1);
                $scope.size = results.data.size;
                $scope.sumAll = results.data.sumAll;
                $scope.noData = results.data.missingData;
                $scope.transactions.forEach(function (t) {
                    t.date = new Date(t.date);
                })
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
