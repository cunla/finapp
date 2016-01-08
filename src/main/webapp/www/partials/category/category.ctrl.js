appCtrllers
    .controller('catCtrl', ['$scope', 'FinApp', '$stateParams', '$rootScope', '$ionicPopup', 'DatePicker',
        function ($scope, FinApp, $stateParams, $rootScope, $ionicPopup, DatePicker) {
            var groupId = $stateParams.groupId;
            //$scope.periodType = "Custom";

            $scope.startDateObject = DatePicker.datePickerObject("Start date", DatePicker.beginningOfMonth());
            $scope.endDateObject = DatePicker.datePickerObject("End date", DatePicker.endOfMonth());
            if ($rootScope.user) {
                $scope.startDateObject.inputDate = $rootScope.user.defaultStart;
                $scope.endDateObject.inputDate = $rootScope.user.defaultEnd;
            }
            $scope.doRefresh = function () {
                FinApp.getCategoryReport(groupId, $scope.startDateObject.inputDate, $scope.endDateObject.inputDate)
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
        }])
