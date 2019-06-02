
App.common.Pagination = function (itemsPerPage, page, totalItems, totalPages) {
    this.itemsPerPage = (typeof itemsPerPage === 'number') ? Math.max(itemsPerPage, 5) : 10;
    this.page = (typeof page === 'number') ? Math.max(page, 1) : 1;
    this.totalItems = (typeof totalItems === 'number')? Math.max(totalItems, 0) : 0;
    this.totalPages = (typeof totalPages === 'number')? Math.max(totalPages, 0) : 0;
    
    this.updateItems = function (items) {
        this.totalItems = items;
        this.totalPages = Math.ceil(items / this.itemsPerPage);
    };
};
