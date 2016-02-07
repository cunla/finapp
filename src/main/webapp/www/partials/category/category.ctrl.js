(function () {
    angular.module('app.controllers')
        .controller('catCtrl', ['$scope', 'fin', '$stateParams', '$rootScope', '$ionicPopup', 'commons', catCtrl])
    function catCtrl($scope, fin, $stateParams, $rootScope, $ionicPopup, commons) {
        var groupId = $stateParams.groupId;
        $scope.colors = commons.colors();
        $scope.icons = commons.icons();
        $scope.monthly = true;
        $scope.startDateObject = commons.datePickerObject("Start date", commons.beginningOfMonth());
        $scope.endDateObject = commons.datePickerObject("End date", commons.endOfMonth());
        if ($rootScope.user) {
            if ($rootScope.user.defaultStart)
                $scope.startDateObject.inputDate = $rootScope.user.defaultStart;
            if ($rootScope.user.defaultEnd)
                $scope.endDateObject.inputDate = $rootScope.user.defaultEnd;
        }
        $scope.togglePeriodSelector = togglePeriodSelector;
        $scope.doRefresh = refresh;
        $scope.toggleAddForm = toggleAddForm;
        $scope.createCategory = createCategory;
        $scope.nextPeriod = nextPeriod;
        $scope.previousPeriod = previousPeriod;
        $scope.refreshPeriod = true;
        $scope.edit = edit;
        $scope.ncat = {};
        $scope.doRefresh();

        function createCategory(cat) {
            $scope.showAddForm = false;
            $scope.editMode = false;
            fin.createCategory(groupId, cat).then(function (res) {

            });
        }

        function previousPeriod() {
            $scope.categories = [];
            $scope.refreshPeriod = true;
            var date = new Date($scope.period.start);
            var start = new Date(date.getFullYear(), date.getMonth() - 1, 1);
            date = new Date($scope.period.end);
            var end = new Date(date.getFullYear(), date.getMonth(), 0);
            getData(start, end);
        }

        function nextPeriod() {
            $scope.categories = [];
            $scope.refreshPeriod = true;
            var date = new Date($scope.period.start);
            var start = new Date(date.getFullYear(), date.getMonth() + 1, 1);
            date = new Date($scope.period.end);
            var end = new Date(date.getFullYear(), date.getMonth() + 1, 0);
            getData(start, end);
        }

        function edit(cat) {
            $scope.ncat = cat;
            $scope.showAddForm = true;
            $scope.editMode = true;
        }

        function togglePeriodSelector() {
            $scope.showPeriodSelector = !$scope.showPeriodSelector;
        }

        function toggleAddForm() {
            $scope.showAddForm = !$scope.showAddForm;
            $scope.editMode = false;
        }

        function refresh() {
            getData($scope.startDateObject.inputDate, $scope.endDateObject.inputDate);
        }

        function getData(start, end) {
            fin.getCategoryReport(groupId, start, end)
                .then(function (res) {
                    afterRefresh(res);
                })
                .finally(function () {
                    $scope.$broadcast('scroll.refreshComplete');
                    $scope.refreshPeriod = false;
                })
        }

        function afterRefresh(res) {
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
        }
    };
})();
