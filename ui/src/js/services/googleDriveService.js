import * as env from "./../constants/environment";

export function getAllFiles() {
  console.log("Fetch URL: ", env.SERVER_HOSTNAME + env.FILES_ENDPOINT);
  return fetch(env.SERVER_HOSTNAME + env.FILES_ENDPOINT);
}

export function getFilesInFolder(folderName) {
  console.log(
    "Fetch URL: ",
    env.SERVER_HOSTNAME + env.FILES_ENDPOINT + "?folderName=" + folderName
  );
  return fetch(
    env.SERVER_HOSTNAME + env.FILES_ENDPOINT + "?folderName=" + folderName
  );
}

export function createFolder(folderName) {
  let data = {
    folderName
  };
  let createFolderEndpoint = env.SERVER_HOSTNAME + env.FOLDERS_ENDPOINT;
  console.log({ createFolderEndpoint, data });
  return fetch(createFolderEndpoint, {
    method: "post",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data)
  });
}

export function uploadFileToFolder(folderName, file) {
  let form = new FormData();
  form.append("file", file);
  form.append("folder", folderName);
  let createFolderEndpoint = env.SERVER_HOSTNAME + env.FILES_ENDPOINT;
  console.log({ createFolderEndpoint, form });
  return fetch(createFolderEndpoint, {
    method: "post",
    body: form
  });
}

export function downloadFile(fileName) {
  window.open(env.SERVER_HOSTNAME + env.FILES_DOWNLOAD_ENDPOINT + fileName);
  return fetch(env.SERVER_HOSTNAME + env.FILES_DOWNLOAD_ENDPOINT + fileName);
}
