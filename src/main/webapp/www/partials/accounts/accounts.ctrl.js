(function () {

    angular.module('app.controllers')
        .controller('accountsCtrl', ['$scope', '$state', 'fin', '$rootScope', 'commons', accountsCtrl]);
    function accountsCtrl($scope, $state, fin, $rootScope, commons) {
        $scope.groupId = $state.params.groupId;
        $scope.colors = commons.colors();
        $scope.icons = commons.accountIcons();
        $scope.doRefresh = refresh;
        $scope.toggleAddForm = function () {
            $scope.showAddForm = !$scope.showAddForm;
        }

        $scope.createAccount = function (acc) {
            $scope.showAddForm = false;
            fin.createAccount(acc).then(function (res) {

            });
        }
        $scope.doRefresh();


        function refresh() {
            fin.getAccountsReport($scope.groupId)
                .then(function (res) {
                    $scope.accounts = res.data;
                    $scope.sumAll = 0;
                    if (!$scope.accounts) {
                        $scope.accounts = [];
                    }
                    var now = new Date();
                    $scope.accounts.forEach(function (t) {
                        t.lastValidated = new Date(t.lastValidated);
                        t.notUpdated = dateDistanceLong(t.lastValidated, now);
                        $scope.sumAll += t.balance;
                    })

                })
                .finally(function () {
                    $scope.$broadcast('scroll.refreshComplete');
                })
        };
    }


    function dateDistanceLong(d1, d2) {
        return Math.abs(d1.getTime() - d2.getTime()) > 1000 * 60 * 60 * 24;
    }
})();
