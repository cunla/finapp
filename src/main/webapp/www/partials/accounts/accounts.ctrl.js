(function () {

    angular.module('app.controllers')
        .controller('accountsCtrl', ['$scope', '$state', 'fin', '$rootScope', accountsCtrl]);
    function accountsCtrl($scope, $state, fin, $rootScope) {
        $scope.groupId = $state.params.groupId;
        $scope.doRefresh = function () {
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
        $scope.doRefresh();

    }

    function dateDistanceLong(d1, d2) {
        return Math.abs(d1.getTime() - d2.getTime()) > 1000 * 60 * 60 * 24;
    }
})();
