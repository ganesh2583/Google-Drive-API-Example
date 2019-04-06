import React, { Component } from "react";
import {
  getFilesInFolder,
  downloadFile
} from "./../services/googleDriveService";
import TableComponent from "./TableComponent";

class FolderContentsComponent extends Component {
  state = {
    listOfFiles: []
  };

  componentDidMount() {
    console.log(this.props);
    getFilesInFolder(this.props.match.params.folderName)
      .then(response => response.json())
      .then(filesList => {
        console.log("Response for all folders files call ", filesList);
        this.setState({ listOfFiles: filesList });
      });
  }

  handleDownload(item) {
    console.log("Download link clicked", item.name);
    downloadFile(item.name);
  }

  render() {
    return (
      <React.Fragment>
        Folder Contents Component {this.props.match.params.folderName}
        <TableComponent
          listOfFiles={this.state.listOfFiles}
          download={this.handleDownload}
        />
      </React.Fragment>
    );
  }
}

export default FolderContentsComponent;
