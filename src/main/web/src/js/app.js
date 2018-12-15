import React, {Component} from "react";
import ReactDOM from "react-dom";
import {Alignment, AnchorButton, Button, ButtonGroup, Card, Elevation, Navbar, Tooltip} from "@blueprintjs/core";
import {Flex, Box} from "reflexbox";
import moment from "moment";

class Timeline extends Component {
    constructor(props) {
        super();
        this.props = props;
        console.log(props);
    }
    render() {
        return (
            <Box px={2} w={472}>
                <TimelineHeader title={this.props.title}/>
                {this.props.articles.map(status => <Article status={status}/>)}
            </Box>
        );
    }
}

function TimelineHeader(props) {
    return (
        <Navbar>
            <Navbar.Group align={Alignment.LEFT}>
                <Navbar.Heading>{props.title}</Navbar.Heading>
                <Navbar.Divider/>
            </Navbar.Group>
            <Navbar.Group align={Alignment.RIGHT}>
                <Button className="bp3-minimal" icon="settings"/>
            </Navbar.Group>
        </Navbar>
    );
}

class Article extends Component {
    constructor(props) {
        super();
        this.props = props;
    }
    render() {
        const data = this.props.status;
        let status = data;

        if (data.reblog) {
            status = data.reblog;
        }

        const statusParts = (
            <div>
                <StatusHeader account={status.account} createdAt={status.createdAt}/>
                <StatusContent spoilerText={status.spoilerText} content={status.content}/>
                <StatusImages attachments={status.attachments}/>
                <StatusActionButtons visibility={status.visibility}
                                 url={status.url}
                                 reblogsCount={status.reblogsCount}
                                 favouritesCount={status.favouritesCount}/>
            </div>
        );

        if (!data.reblog) {
            return (
                <Card elevation={Elevation.TWO}>
                    {statusParts}
                </Card>
            );
        } else {
            return (
                <Card elevation={Elevation.TWO}>
                    <StatusHeader account={data.account} createdAt={data.createdAt}/>
                    {statusParts}
                </Card>
            );
        }
    }
}

function StatusHeader(props) {
    return (
        <Navbar style={{paddingLeft: "0px", paddingRight: "5px"}}>
            <Navbar.Group align={Alignment.LEFT}>
                <Avatar account={props.account}/>
                <Navbar.Divider />
                <a style={{float: "left"}} target="_blank" href={props.account.url} title={props.account.acct}>
                    <span>
                        <span style={{textDecoration: "none"}}><bdi><strong>{props.account.username}</strong></bdi></span><br/>
                        <span>{props.account.acct}</span>
                    </span>
                </a>
            </Navbar.Group>
            <Navbar.Group align={Alignment.RIGHT}>
                <StatusDate date={props.createdAt}/>
            </Navbar.Group>
        </Navbar>
    );
}

function StatusDate(props) {
    const date = moment(props.date);
    return (
        <time style={{float: "right"}} dateTime={date.toISOString()} title={date.format()}>{date.fromNow()}</time>
    )
}

function Avatar(props) {
    return (
        <div style={{
            // marginLeft: "1px",
            // marginRight: "1px",
            // marginTop: "1px",
            // marginBottom: "1px",
            float: "left",
            width: "50px",
            height: "50px",
            backgroundSize: "50px 50px",
            backgroundImage: "url(\"" + props.account.avatar + "\")"
        }}/>
    );
}

class StatusContent extends Component {
    constructor(props) {
        super();
        this.props = props;
        this.showCW = !!props.spoilerText;
        this.showStatus = !this.showCW;
    }
    buttonText() {
        return this.showStatus ? "Show less" : "Show more";
    }
    toggle() {
        this.setState(() => {
            this.showStatus = !this.showStatus;
            return this.props;
        });
    }
    render() {
        console.log("Rendering: ", this.showCW, this.showStatus, this.props.content);
        return (
            <div>
                <p hidden={!this.showCW}><span>{this.props.spoilerText}</span>
                    <Button text={this.buttonText()} onClick={() => this.toggle()}/>
                </p>
                <p hidden={!this.showStatus}>
                    <bdi>
                        <div dangerouslySetInnerHTML={{__html: this.props.content}}>{}</div>
                    </bdi>
                </p>
            </div>
        );
    }
}

function StatusImages(props) {
    const imageStyle = {marginLeft: "auto", marginRight: "auto", display: "block"};
    const imageList = props.attachments.map((attachment) => <img src={attachment.previewUrl} style={imageStyle}/>);
    return (
        <div style={{marginTop: "10px", marginBottom: "10px"}}>
            {imageList}
        </div>
    )
}

function StatusActionButtons(props) {
    const isDisabled = props.url === null || props.url === undefined;
    let visibilityIcon = props.visibility === "public" ? "globe" :
        (props.visibility === "unlisted" ? "unlock" :
            (props.visibility === "followers-only" ? "lock" :
            (props.visibility === "direct" ? "envelope" : "help")));
    return (
        <div>
            <ButtonGroup>
                <Tooltip content={props.visibility}>
                    <Button icon={visibilityIcon} text={visibilityIcon === "help" ? props.visibility : ""}/>
                </Tooltip>
                <AnchorButton icon={"link"} href={props.url} target={"_blank"} rel={"noopener"} disabled={isDisabled}/>
                <Button icon={"comment"}/>
                <Button icon={"share"} text={props.reblogsCount}/>
                <Button icon={"star-empty"} text={props.favouritesCount}/>
                <Button icon={"more"}/>
            </ButtonGroup>
        </div>
    );
}

class FormContainer extends Component {
    constructor(props) {
        super();
        this.props = props;
    }
    render() {
        const timelines = this.props.timelines;
        return (
            <Flex p={2}>
                {Object.keys(timelines).map(key => <Timeline title={key} articles={timelines[key]}></Timeline>)}
            </Flex>
        );
    }
}

Promise.all([
    fetch("/status").then(response => response.json()),
    fetch("/boosts").then(response => response.json())
])
    .then(json => {
        const timelines = {
            home: json[0],
            boosts: json[1]
        };
        ReactDOM.render(
            <FormContainer timelines={timelines}/>,
            document.getElementById("react-root")
        );
    });


