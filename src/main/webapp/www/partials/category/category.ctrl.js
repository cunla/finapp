(function () {
    angular.module('app.controllers')
        .controller('catCtrl', ['$scope', 'fin', '$stateParams', '$rootScope', '$ionicPopup', 'commons', catCtrl])
    function catCtrl($scope, fin, $stateParams, $rootScope, $ionicPopup, commons) {
        var groupId = $stateParams.groupId;
        $scope.colors = commons.colors();
        $scope.icons = commons.icons();
        $scope.startDateObject = commons.datePickerObject("Start date", commons.beginningOfMonth());
        $scope.endDateObject = commons.datePickerObject("End date", commons.endOfMonth());
        if ($rootScope.user) {
            if ($rootScope.user.defaultStart)
                $scope.startDateObject.inputDate = $rootScope.user.defaultStart;
            if ($rootScope.user.defaultEnd)
                $scope.endDateObject.inputDate = $rootScope.user.defaultEnd;
        }
        $scope.doRefresh = refresh;
        $scope.toggleAddForm = function () {
            $scope.showAddForm = !$scope.showAddForm;
        }
        $scope.ncat = {};
        $scope.createCategory = function (cat) {
            $scope.showAddForm = false;
            fin.createCategory(groupId,cat).then(function (res) {

            });
        }

        $scope.doRefresh();

        function refresh() {
            fin.getCategoryReport(groupId, $scope.startDateObject.inputDate, $scope.endDateObject.inputDate)
                .then(function (res) {
                    $scope.period = res.data.period;
                    $scope.categories = res.data.category;
                    $scope.sumAll = 0;
                    if ($scope.categories) {
                        $scope.categories.forEach(function (t) {
                            $scope.sumAll += t.total;
                        })
                    } else {
                        $scope.categories = [];
                    }
                })
                .finally(function () {
                    $scope.$broadcast('scroll.refreshComplete');
                })
        }
    };
})();
