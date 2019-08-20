/*
 *   Note: in production, the specific packages from the libraries below should be selected to minimize
 *     ..: the overall bundle size.
 */

import * as antd from "antd";
import * as MaterialUI from "@material-ui/core";
import * as MaterialIcons from "@material-ui/icons";

require("bootstrap/dist/css/bootstrap.css");
require("antd/dist/antd.css");


window.antd = antd;
window.antMessage = antd.message;
window.antMessage.success = antd.message.success;
window.MaterialUI = MaterialUI;
window.MaterialIcons = MaterialIcons;
