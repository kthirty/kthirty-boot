import type { #(entityName)Api } from './api';

import type { VbenFormSchema } from '#/adapter/form';
import type { OnActionClickFn, VxeTableGridOptions } from '#/adapter/vxe-table';

import { useDictStore } from '#/store/dict';
import { $t } from '#/locales';

const dictStore = useDictStore();

export function useSearchSchema(): VbenFormSchema[] {
  return [
#for(field : queryFields)
    {
      fieldName: '#(field.fieldName)',
      label: '#(field.label)',
      component: '#(field.component)',
#if(field.hasDict)
      componentProps: {
        allowClear: true,
        options: dictStore.getDict('#(field.dictCode)'),
      },
#end
    },
#end
  ];
}

export function useColumns(
  onActionClick: OnActionClickFn<#(entityName)Api.#(entityName)>,
): VxeTableGridOptions['columns'] {
  return [
#for(field : listFields)
    {
      field: '#(field.listFieldName)',
      title: '#(field.label)',
      minWidth: 120,
    },
#end
    {
      align: 'center',
      cellRender: {
        attrs: {
          nameField: '#(nameField)',
          nameTitle: '#(title)',
          onClick: onActionClick,
        },
        options: ['edit', 'delete'],
        name: 'CellOperation',
      },
      field: 'operation',
      fixed: 'right',
      title: '操作',
      width: 140,
    },
  ];
}

export function useFormSchema(): VbenFormSchema[] {
  return [
#for(field : formFields)
    {
      fieldName: '#(field.fieldName)',
      label: '#(field.label)',
      component: '#(field.component)',
#if(field.required)
      rules: 'required',
#end
#if(field.hasDict)
      componentProps: {
        allowClear: true,
        options: dictStore.getDict('#(field.dictCode)'),
      },
#end
#if(field.defaultValue != null && field.defaultValue != "")
      defaultValue: '#(field.defaultValue)',
#end
    },
#end
  ];
}
