$(function() {

    $.extend($.jgrid.defaults, {
        autowidth: true,
        shrinkToFit: true,
        datatype: 'json',
        jsonReader: {
            repeatitems: false,
            root: "content",
            page: function(result) {
                //Total number of records
                return result.number + 1;
            },
            total: "totalPages",
            records: "totalElements"
        },
        prmNames: {
            page: "page.page",
            rows: "page.size",
            sort: "page.sort",
            order: "page.sort.dir"
        },
        height: 'auto',
        viewrecords: true,
        rowList: [10, 20, 50, 100],
        altRows: true,
        loadError: function(xhr, status, error) {

        }
    });

    $.extend($.jgrid.edit, {
        closeAfterEdit: true,
        closeAfterAdd: true,
        ajaxEditOptions: {contentType: "application/json"},
        mtype: 'PUT',
        serializeEditData: function(data) {
            delete data.oper;
            return JSON.stringify(data);
        }
    });
    $.extend($.jgrid.del, {
        mtype: 'DELETE',
        serializeDelData: function() {
            return "";
        }
    });
    var delOptions = {
        onclickSubmit: function(params, postdata) {
            params.url = URL + '/' + postdata;
        }
    };

    var URL = 'api/entities';
    var options = {
        url: URL,
        editurl: URL,
        colModel: [
            {
                name: 'name',
                label: 'Name',
                key: true,
                index: 'name',
                editable: true,
                editoptions: {required: true}
            },
            {
                name: 'label',
                label: 'Label',
                index: 'label',
                editable: true,
                editrules: {required: true}
            },
            {
                name: 'description',
                label: 'Description',
                index: 'description',
                editable: true,
                editrules: {required: true}
            }
        ],
        caption: "entities",
        pager: '#pager',
        height: 'auto',
        ondblClickRow: function(id) {
            window.location = 'entities/' + id;
        },
        formatter: {idName: "name"}
    };

    $("#grid")
            .jqGrid(options)
            .navGrid('#pager',
                    {search: false, addfunc: function() {
                            window.location = 'entities/add';
                        }, editfunc: function(data) {
                            window.location = 'entities/' + data;
                        }}, //options
            {}, // edit options
                    {}, // add options 
                    delOptions,
                    {} // search options
            );
});
