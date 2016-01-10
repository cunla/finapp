(function () {
    angular.module('app.controllers')
        .controller('catCtrl', ['$scope', 'fin', '$stateParams', '$rootScope', '$ionicPopup', 'datePicker', catCtrl])
    function catCtrl($scope, fin, $stateParams, $rootScope, $ionicPopup, datePicker) {
        var groupId = $stateParams.groupId;
        //$scope.periodType = "Custom";

        $scope.startDateObject = datePicker.datePickerObject("Start date", datePicker.beginningOfMonth());
        $scope.endDateObject = datePicker.datePickerObject("End date", datePicker.endOfMonth());
        if ($rootScope.user) {
            $scope.startDateObject.inputDate = $rootScope.user.defaultStart;
            $scope.endDateObject.inputDate = $rootScope.user.defaultEnd;
        }
        $scope.doRefresh = function () {
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

        $scope.doRefresh();
    };
})();
