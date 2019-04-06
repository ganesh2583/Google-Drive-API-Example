import React, { Component } from "react";
import { getAllFiles, downloadFile } from "./../services/googleDriveService";
import { Link } from "react-router-dom";
import TableComponent from "./TableComponent";

class HomeComponent extends Component {
  state = {
    listOfFiles: []
  };

  componentDidMount() {
    getAllFiles()
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
        <h1>This is Home page component</h1>
        <TableComponent
          listOfFiles={this.state.listOfFiles}
          download={this.handleDownload}
        />
      </React.Fragment>
    );
  }
}

export default HomeComponent;
