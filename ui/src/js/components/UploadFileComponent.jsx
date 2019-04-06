import React, { Component } from "react";
import { uploadFileToFolder } from "./../services/googleDriveService";

class UploadFileComponent extends Component {
  state = {
    foldername: "",
    file: "",
    intialRender: true,
    isFileUploaded: false,
    createdFileId: ""
  };

  handleUploadFileEvent = e => {
    let file = e.target.files[0];
    this.setState({ file });
    console.log({ file });
  };

  handleFolderNameChnage = e => {
    let foldername = e.target.value;
    this.setState({ foldername });
  };

  handleUploadFile = () => {
    console.log("File upload clicked..!!");
    uploadFileToFolder(this.state.foldername, this.state.file)
      .then(response => {
        console.log("Response from server: ", response);
        if (response.status === 200) {
          this.setState({
            isFileUploaded: !this.state.isFileUploaded,
            intialRender: false
          });
          return response.text();
        } else {
          this.setState({
            isFileUploaded: !this.state.isFileUploaded,
            intialRender: false
          });
          return "No File Id";
        }
      })
      .then(folderId => {
        console.log({ folderId });
        this.setState({ createdFileId: folderId });
      });
  };
  render() {
    return (
      <React.Fragment>
        <h1>This is Upload File component</h1>
        <div className="form-group for-inline">
          <input
            type="file"
            className="form-control"
            placeholder="Upload the File..."
            onChange={this.handleUploadFileEvent}
          />
        </div>
        <div className="form-group">
          <input
            type="text"
            className="form-control"
            placeholder="Folder name.."
            onChange={this.handleFolderNameChnage}
          />
        </div>
        <div className="form-group col-xs-offset-5">
          <button
            type="button"
            className="btn btn-primary btn-sm"
            onClick={this.handleUploadFile}
          >
            Upload File
          </button>
        </div>
        {this.state.isFileUploaded && (
          <div className="alert alert-success">
            <strong>
              File Uploaded successfully. Pleas check your Google Drive!
            </strong>
          </div>
        )}
        {!this.state.intialRender && !this.state.isFileUploaded && (
          <div className="alert alert-danger">
            <strong>Oops!! Something went wrong. Please try later.</strong>
          </div>
        )}
      </React.Fragment>
    );
  }
}

export default UploadFileComponent;
