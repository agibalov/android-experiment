android-custom-layout-loader-experiment
=======================================

Here's how the layout is defined:

    {
	    "type": "container",
	    "id": 23021,
	    "children": [
		    {
			    "type": "label",
			    "id": 23022,
			    "text": "my label"
		    },
		    {
			    "type": "button",
			    "id": 23023,
			    "text": "my button"
		    }
	    ]
    }
    
Here's how this definition gets interpreted:

    LinearLayout
      TextView("my label")
      Button("my button")
