(function () {

    angular.module('app.controllers')
        .controller('accountsCtrl', ['$scope', '$state', 'fin', '$rootScope', 'commons', accountsCtrl]);
    function accountsCtrl($scope, $state, fin, $rootScope, commons) {
        $scope.groupId = $state.params.groupId;
        $scope.colors = commons.colors();
        $scope.icons = commons.accountIcons();
        $scope.doRefresh = refresh;
        $scope.toggleAddForm = toggleAddForm;
        $scope.createAccount = createAccount;
        $scope.edit = edit;
        $scope.doRefresh();

        function createAccount(acc) {
            $scope.showAddForm = false;
            $scope.editMode = false;
            fin.createAccount($scope.groupId, acc).then(function (res) {

            });
        }

        function edit(acc) {
            $scope.nacc = acc;
            $scope.showAddForm = true;
            $scope.editMode = true;
        }

        function toggleAddForm() {
            $scope.showAddForm = !$scope.showAddForm;
        }

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
