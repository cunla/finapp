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
        $scope.toggleAddForm = toggleAddForm;
        $scope.createCategory = createCategory;
        $scope.edit = edit;
        $scope.ncat = {};
        $scope.doRefresh();

        function createCategory(cat) {
            $scope.showAddForm = false;
            $scope.editMode = false;
            fin.createCategory(groupId, cat).then(function (res) {

            });
        }

        function edit(cat) {
            $scope.ncat = cat;
            $scope.showAddForm = true;
            $scope.editMode = true;
        }

        function toggleAddForm() {
            $scope.showAddForm = !$scope.showAddForm;
            $scope.editMode = false;
        }

        function refresh() {
            fin.getCategoryReport(groupId, $scope.startDateObject.inputDate, $scope.endDateObject.inputDate)
                .then(function (res) {
                    $scope.period = res.data.period;
                    $scope.categories = res.data.categories;
                    $scope.withoutCategory = res.data.transactionsWithoutCategory;
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
