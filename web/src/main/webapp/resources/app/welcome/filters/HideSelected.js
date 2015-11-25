angular
    .module('welcomeModule')
    .filter('hideSelected', function () {
        return function (items, selectedItem) {
            if (selectedItem){
                var index = items.indexOf(selectedItem);
                if (index >= -1) {
                    items.splice(index, 1);
                }
            }
            return items;
        }
    });