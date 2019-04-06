import React, { Component } from "react";
import "./App.css";
import Homepage from "./js/pages/Homepage";
import { BrowserRouter, Route } from "react-router-dom";
import CreateFolderPage from "./js/pages/CreateFolderpage";
import UploadFilePage from "./js/pages/UploadFilePage";
import Header from "./js/components/Header";
import Footer from "./js/components/Footer";
import FolderContentsComponent from "./js/components/FolderContents";

class App extends Component {
  render() {
    return (
      <BrowserRouter>
        <div className="container">
          <div className="col-xs-12">
            <Header />
          </div>
          <br />
          <br />
          <br />
          <br />
          <main role="main" className="container">
            <div className="col-xs-8 col-sm-offset-2">
              <Route path={"/"} exact component={Homepage} />
              <Route path={"/home"} exact component={Homepage} />
              <Route
                path={"/createFolder"}
                exact
                component={CreateFolderPage}
              />
              <Route path={"/uploadFile"} exact component={UploadFilePage} />
              <Route
                exact
                path="/home/:folderName"
                render={props => <FolderContentsComponent {...props} />}
              />
            </div>
          </main>
          <div className="col-xs-12">
            <Footer />
          </div>
        </div>
      </BrowserRouter>
    );
  }
}

export default App;
