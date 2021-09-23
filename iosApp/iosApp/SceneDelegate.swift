//
//  SceneDelegate.swift
//  iosApp
//
//  Created by Administrator on 02.09.2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import UIKit


class SceneDelegate : UIResponder ,UISceneDelegate{
    
    var window: UIWindow?
    
    @available(iOS 13.0, *)
    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        
        guard let windowScene = (scene as? UIWindowScene) else { return }
        
        /// 2. Create a new UIWindow using the windowScene constructor which takes in a window scene.
        let window = UIWindow(windowScene: windowScene)
        
        
        /// 3. Create a view hierarchy programmatically
        let viewController = GitHubUserViewController()
        
        viewController.view.backgroundColor = .white
        let navigation = UINavigationController(rootViewController: viewController)
        
        /// 4. Set the root view controller of the window with your view controller
        window.rootViewController = navigation
        
        /// 5. Set the window and call makeKeyAndVisible()
        self.window = window
        window.makeKeyAndVisible()
    }
    
}

