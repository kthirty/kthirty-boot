import { requestClient } from '#/api/request';

export namespace #(entityName)Api {
  export interface #(entityName) {
    id?: string;
    [key: string]: any;
  }
}

export async function get#(entityName)Page(params?: Record<string, any>) {
  return requestClient.get('#(apiPrefix)/page', { params });
}

export async function get#(entityName)List(params?: Record<string, any>) {
  return requestClient.get('#(apiPrefix)/list', { params });
}

export async function get#(entityName)Info(id: string) {
  return requestClient.get(`#(apiPrefix)/getInfo/${id}`);
}

export async function save#(entityName)(data: #(entityName)Api.#(entityName)) {
  return requestClient.post('#(apiPrefix)/save', data);
}

export async function update#(entityName)(data: #(entityName)Api.#(entityName)) {
  return requestClient.put('#(apiPrefix)/update', data);
}

export async function remove#(entityName)(id: string) {
  return requestClient.delete(`#(apiPrefix)/remove/${id}`);
}
