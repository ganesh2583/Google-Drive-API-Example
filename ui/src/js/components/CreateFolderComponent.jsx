import React, { Component } from "react";
import { createFolder } from "./../services/googleDriveService";

class CreateFolderCoponent extends Component {
  state = {
    foldername: "",
    intialRender: true,
    isFolderCreated: false,
    createdFolderId: ""
  };

  handleCreateFolder = () => {
    console.log("Create folder clicked", this.state.foldername);
    createFolder(this.state.foldername)
      .then(response => {
        console.log("Response from server: ", response);
        if (response.status === 200) {
          this.setState({
            isFolderCreated: !this.state.isFolderCreated,
            intialRender: false
          });
          return response.text();
        } else {
          this.setState({
            isFolderCreated: !this.state.isFolderCreated,
            intialRender: false
          });
          return "No Folder Id";
        }
      })
      .then(folderId => {
        console.log({ folderId });
        this.setState({ createdFolderId: folderId });
      });
  };

  handleFolderNameChnage = e => {
    let foldername = e.target.value;
    this.setState({ foldername });
  };

  render() {
    return (
      <React.Fragment>
        <h1>Create Folder in Google Drive... </h1>
        <br />
        <div className="form-group">
          <input
            type="text"
            className="form-control"
            placeholder="Enter Folder name to be creaed..."
            onChange={this.handleFolderNameChnage}
          />
        </div>
        <div className="form-group col-xs-offset-5">
          <button
            type="button"
            className="btn btn-primary btn-sm"
            onClick={this.handleCreateFolder}
          >
            Add Folder
          </button>
        </div>
        {this.state.isFolderCreated && (
          <div className="alert alert-success">
            <strong>
              Folder Created successfully. Pleas check your Google Drive!
            </strong>
          </div>
        )}
        {!this.state.intialRender && !this.state.isFolderCreated && (
          <div className="alert alert-danger">
            <strong>Oops!! Something went wrong. Please try later.</strong>
          </div>
        )}
      </React.Fragment>
    );
  }
}

export default CreateFolderCoponent;
