(function () {
    angular.module('app.controllers')
        .controller('catCtrl', ['$scope', 'fin', '$stateParams', '$rootScope', '$ionicPopup', 'datePicker', catCtrl])
    function catCtrl($scope, fin, $stateParams, $rootScope, $ionicPopup, datePicker) {
        var groupId = $stateParams.groupId;
        $scope.colors = colors();
        $scope.icons = icons();
        $scope.startDateObject = datePicker.datePickerObject("Start date", datePicker.beginningOfMonth());
        $scope.endDateObject = datePicker.datePickerObject("End date", datePicker.endOfMonth());
        if ($rootScope.user) {
            if ($rootScope.user.defaultStart)
                $scope.startDateObject.inputDate = $rootScope.user.defaultStart;
            if ($rootScope.user.defaultEnd)
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
        $scope.toggleAddForm = function () {
            $scope.showAddForm = !$scope.showAddForm;
        }
        $scope.ncat = {};
        $scope.createCategory = function (cat) {
            $scope.showAddForm = false;
            fin.createCategory(cat).then(function (res) {

            });
        }

        $scope.doRefresh();
    };
    function colors() {
        return ['stable', 'positive', 'calm', 'balanced', 'energized', 'assertive', 'royal', 'dark'];
    };
    function icons() {
        return ['ion-bag',
            'ion-ios-cart',
            'ion-home',
            'ion-earth',
            'ion-model-s',
            'ion-card',
            'ion-social-bitcoin-outline',
            'ion-cash',
            'ion-social-usd',
            'ion-social-facebook',
            'ion-social-euro',
            'ion-social-tux',
            'ion-social-apple',
            'ion-social-android',
            'ion-record'
        ];
    }
})();
