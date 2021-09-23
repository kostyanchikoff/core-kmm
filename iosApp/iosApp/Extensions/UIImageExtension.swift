//
//  UIImageExtension.swift
//  iosApp
//
//  Created by Administrator on 02.10.2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import UIKit

extension UIImageView {
    
    var newtwotkUrl : String {
        get{
            return self.newtwotkUrl
        }
        set{
           
            let url = URL.init(string: newValue)
                   DispatchQueue.global().async { [weak self] in
                       if let data = try? Data(contentsOf: url!) {
                           if let image = UIImage(data: data) {
                               DispatchQueue.main.async {
                                   self?.image = image
                               }
                           }
                       }
                   }
               
        }
    }
    
}
