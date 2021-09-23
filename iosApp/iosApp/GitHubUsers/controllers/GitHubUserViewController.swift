//
//  GitHubUserViewController.swift
//  iosApp
//
//  Created by Administrator on 27.09.2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import UIKit
import shared


class GitHubUserViewController : UIViewController{
    
    private let viewModel = GitHubUsersViewModel()
    private let cellId = "gitHubUserCellId"
    private var gitHubUsers : [GitHubUser] = [GitHubUser]()
    
    lazy var  usersCollectionView  : UITableView = {
        let view = UITableView()
        view.delegate = self
        view.dataSource = self
        view.register(GitHubUserViewCell.self, forCellReuseIdentifier: cellId)
        view.separatorStyle = .none
        return view;
    }()
    
    lazy var progressView : UIActivityIndicatorView = {
       let view = UIActivityIndicatorView()
        return view
    }()
    

    override func viewDidLoad() {
        observeViewState()
        addSubView()
        setupConstaints()
        viewModel.setEvent(event:  GitHubUserEvent.GetUsers())
    }
    

    private func observeViewState(){
        viewModel.viewState.observe(value: { (state : CoreViewState)  in
        
        
            if(state is GitHubUserState.LoadingState){
                self.progressView.startAnimating()
                return
            }

            if(state is GitHubUserState.LoadedState){
                self.progressView.stopAnimating()
                let loadedState = state as! GitHubUserState.LoadedState
                self.gitHubUsers = loadedState.users
                self.usersCollectionView.reloadData()
                return
            }
        })
    }
    
    
    private func addSubView(){
        view.addSubview(usersCollectionView)
        view.addSubview(progressView);
    
    }
    
    private func setupConstaints(){
        
        progressView.translatesAutoresizingMaskIntoConstraints = false
        progressView.centerYAnchor.constraint(equalTo: view.centerYAnchor).isActive = true
        progressView.centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
        
        usersCollectionView.translatesAutoresizingMaskIntoConstraints = false
        usersCollectionView.topAnchor.constraint(equalTo: view.topAnchor).isActive = true
        usersCollectionView.leftAnchor.constraint(equalTo: view.leftAnchor).isActive = true
        usersCollectionView.rightAnchor.constraint(equalTo: view.rightAnchor).isActive = true
        usersCollectionView.bottomAnchor.constraint(equalTo: view.bottomAnchor).isActive = true
    }
    

    
    
}


extension GitHubUserViewController : UITableViewDelegate, UITableViewDataSource{
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 350
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return gitHubUsers.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let viewCell = tableView.dequeueReusableCell(withIdentifier: cellId, for: indexPath) as! GitHubUserViewCell
        let item = gitHubUsers[indexPath.row]
        viewCell.avatarImageView.newtwotkUrl = item.avatarUrl
        return viewCell
    }
    
}


class GitHubUserViewCell : UITableViewCell{
    
    
    lazy var avatarImageView : UIImageView = {
        let view = UIImageView()
        view.layer.cornerRadius = 20
        view.widthAnchor.constraint(equalToConstant:  CGFloat(200)).isActive = true
        view.contentMode = .scaleToFill
        return view
    }()
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        addView()
        setupConstraints()
    }
   
    
    private func addView(){
        contentView.addSubview(avatarImageView)
    }
    
    private func setupConstraints(){
        avatarImageView.translatesAutoresizingMaskIntoConstraints = false
        avatarImageView.topAnchor.constraint(equalTo: contentView.topAnchor).isActive = true
        avatarImageView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor).isActive = true
        avatarImageView.leftAnchor.constraint(equalTo: contentView.leftAnchor).isActive = true
        avatarImageView.rightAnchor.constraint(equalTo: contentView.rightAnchor).isActive = true
    }
    
    
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
}


