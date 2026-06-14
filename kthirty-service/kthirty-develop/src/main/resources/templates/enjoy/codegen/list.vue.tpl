<script lang="ts" setup>
import type { #(entityName)Api } from './api';
import type { VxeTableGridOptions } from '#/adapter/vxe-table';

import { Page, useVbenDrawer } from '@vben/common-ui';
import { Plus } from '@vben/icons';
import { Button, message } from 'ant-design-vue';
import { useVbenVxeGrid } from '#/adapter/vxe-table';
import { $t } from '#/locales';

import { get#(entityName)Page, get#(entityName)List, remove#(entityName) } from './api';
import { useColumns, useSearchSchema } from './data';
import Form from './modules/form.vue';

const [FormDrawer, formDrawerApi] = useVbenDrawer({
  connectedComponent: Form,
  destroyOnClose: true,
});

function onCreate() {
  formDrawerApi.setData({}).open();
}

function onEdit(row: #(entityName)Api.#(entityName)) {
  formDrawerApi.setData(row).open();
}

function onDelete(row: #(entityName)Api.#(entityName)) {
  const hideLoading = message.loading({
    content: $t('ui.actionMessage.deleting', [row.#(nameField)]),
    duration: 0,
    key: 'action_process_msg',
  });
  remove#(entityName)(row.id!)
    .then(() => {
      message.success({
        content: $t('ui.actionMessage.deleteSuccess', [row.#(nameField)]),
        key: 'action_process_msg',
      });
      refreshGrid();
    })
    .finally(() => hideLoading());
}

function onActionClick({ code, row }: { code: string; row: #(entityName)Api.#(entityName) }) {
  switch (code) {
    case 'delete':
      onDelete(row);
      break;
    case 'edit':
      onEdit(row);
      break;
  }
}

const [Grid, gridApi] = useVbenVxeGrid({
  formOptions: {
    showCollapseButton: true,
    schema: useSearchSchema(),
    submitOnEnter: true,
  },
  gridOptions: {
    columns: useColumns(onActionClick),
    height: 'auto',
    keepSource: true,
#if(treeList)
    treeConfig: {
      parentField: '#(treeParentField)',
      rowField: '#(treeRowField)',
      transform: false,
      expandAll: true,
    },
    pagerConfig: { enabled: false },
#end
    proxyConfig: {
      ajax: {
#if(treeList)
        query: async (_params, formValues) => {
          const records = await get#(entityName)List(formValues);
          return { records, totalRow: records.length };
        },
#else
        query: async ({ page }, formValues) => {
          return await get#(entityName)Page({
            pageNumber: page.currentPage,
            pageSize: page.pageSize,
            ...formValues,
          });
        },
#end
      },
    },
    rowConfig: { keyField: 'id' },
    toolbarConfig: {
      custom: true,
      export: false,
      refresh: { code: 'query' },
      zoom: true,
    },
  } as VxeTableGridOptions,
});

async function refreshGrid() {
  await gridApi.query();
#if(treeList)
  gridApi.grid?.setAllTreeExpand(true);
#end
}
</script>

<template>
  <Page auto-content-height>
    <FormDrawer @success="refreshGrid" />
    <Grid table-title="#(title)">
      <template #toolbar-tools>
        <Button type="primary" @click="onCreate">
          <Plus class="size-5" />
          {{ $t('ui.actionTitle.create', ['#(title)']) }}
        </Button>
      </template>
    </Grid>
  </Page>
</template>
