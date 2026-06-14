<script lang="ts" setup>
import type { #(entityName)Api } from '../api';
import { computed, ref } from 'vue';
import { useVbenDrawer } from '@vben/common-ui';
import { useVbenForm } from '#/adapter/form';
import { $t } from '#/locales';
import { get#(entityName)Info, save#(entityName), update#(entityName) } from '../api';
import { useFormSchema } from '../data';

const emits = defineEmits(['success']);
const formData = ref<#(entityName)Api.#(entityName)>();
const id = ref<string>();

const [Form, formApi] = useVbenForm({
  commonConfig: { componentProps: { class: 'w-full' } },
  schema: useFormSchema(),
  showDefaultActions: false,
  wrapperClass: 'grid-cols-2',
});

const [Drawer, drawerApi] = useVbenDrawer({
  async onConfirm() {
    const { valid } = await formApi.validate();
    if (!valid) return;
    const values = await formApi.getValues<#(entityName)Api.#(entityName)>();
    drawerApi.lock();
    (id.value ? update#(entityName)({ ...values, id: id.value }) : save#(entityName)(values))
      .then(() => {
        emits('success');
        drawerApi.close();
      })
      .catch(() => drawerApi.unlock());
  },
  onOpenChange(isOpen) {
    if (isOpen) {
      const data = drawerApi.getData<#(entityName)Api.#(entityName)>();
      formApi.resetForm();
      if (data?.id) {
        id.value = data.id;
        get#(entityName)Info(data.id).then((res) => {
          formData.value = res;
          formApi.setValues(res);
        });
      } else {
        id.value = undefined;
        formData.value = data;
        if (data) formApi.setValues(data);
      }
    }
  },
});

const drawerTitle = computed(() =>
  id.value
    ? $t('ui.actionTitle.edit', ['#(title)'])
    : $t('ui.actionTitle.create', ['#(title)']),
);
</script>

<template>
  <Drawer :title="drawerTitle" class="w-[720px]">
    <Form />
  </Drawer>
</template>
