import React, {Component} from "react";
import ReactDOM from "react-dom";
import {Navbar, Button, Card, Alignment, Elevation} from "@blueprintjs/core";
import {Flex, Box} from "reflexbox";

class Timeline extends Component {
    constructor(props) {
        super();
        this.props = props;
        console.log(props);
    }
    render() {
        return (
            <Box px={2} w={320}>
                <TimelineHeader/>
                {this.props.articles.map(status => <Article status={status}/>)}
            </Box>
        );
    }
}

class TimelineHeader extends Component {
    render() {
        return (
            <Navbar>
                <Navbar.Group align={Alignment.LEFT}>
                    <Navbar.Heading><Button className="bp3-minimal" icon="home" text="Home"/>
                        Timeline Title
                    </Navbar.Heading>
                    <Navbar.Divider/>
                </Navbar.Group>
                <Navbar.Group align={Alignment.RIGHT}>
                    <Button className="bp3-minimal" icon="settings"/>
                </Navbar.Group>
            </Navbar>
        );
    }
}

class Article extends Component {
    constructor(props) {
        super();
        this.props = props;
    }
    render() {
        const data = this.props.status;
        // const data = {
        //     account: {
        //         acct: "maloki@elekk.xyz",
        //         url: "https://elekk.xyz/@maloki",
        //         username: "maloki"
        //     },
        //     content: "<p>Bodies are weird.. Like this generalized anxiety is so fucked most of the time...</p>\n" +
        //     "<p>I don't necessarily know where it's coming from, but all of a sudden my chest is hurting and my leg is jumping..</p>",
        //     createdAt: "2018-09-01T15:01:53.000Z",
        //     spoilerText: "Anxiety"
        // };
        const date = new Date(data.createdAt);
        return (
            <Card interactive={true} elevation={Elevation.TWO}>
                <h5 style={{margin: "0px"}}>
                        <div style={{
                            float: "left",
                            width: "48px",
                            height: "48px",
                            backgroundSize: "48px 48px",
                            backgroundImage: "url(\"" + data.account.avatar + "\")"
                        }}></div>
                    <div>
                            <div>
                                <a href="https://elekk.xyz/@maloki/100651113717520579" target="_blank" rel="noopener">Card heading</a>
                            </div>
                            <div>
                                <a style={{float: "left"}} target="_blank" href={data.account.url} title={data.account.acct}>
                                    <span><bdi><strong>{data.account.username}</strong></bdi> <span>{data.account.acct}</span></span>
                                </a>
                                <time style={{float: "right"}} dateTime={data.createdAt} title={date}>5m</time>
                            </div>
                    </div>
                    <div style={{clear: "both"}}></div>
                </h5>
                <div>
                    <p><span>{data.spoilerText}</span>
                        <Button text={"Show less"}/>
                    </p>
                    <ArticleContent content={data.content}/>
                </div>
                <ArticleActionButtons/>
            </Card>
        );
    }
}

function ArticleContent(props) {
    return (
        <div>{props.content}</div>
    );
}

function ArticleActionButtons() {
    return (
        <div>
            <Button icon={"comment"}/>
            <Button icon={"share"}/>
            <Button icon={"star-empty"}/>
            <Button icon={"more"}/>
        </div>
    );
}

class FormContainer extends Component {
    constructor(props) {
        super();
        this.props = props;
    }
    render() {
        return (
            <Flex p={2} align="center">
                <Timeline articles={this.props.articles}></Timeline>
            </Flex>
        );
    }
}

fetch("/status")
    .then(response => response.json())
    .then(json => {
        ReactDOM.render(
            <FormContainer articles={json}/>, document.getElementById("react-root")
        );
    });


